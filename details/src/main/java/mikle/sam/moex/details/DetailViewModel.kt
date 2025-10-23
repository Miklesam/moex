package mikle.sam.moex.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.Security
import mikle.sam.moex.network.Candle
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val apiService: MoexApiService
) : ViewModel() {
    var security by mutableStateOf<Security?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var candles by mutableStateOf<List<Candle>>(emptyList())
        private set
    var candlesErrorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchSecurity(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiService.getSecurity(ticker)
                security = parseSecurity(response)
                errorMessage = null
            } catch (e: Exception) {
                errorMessage = "Failed to load data: ${e.message}"
            }
        }
    }

    fun fetchCandles(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Get last 30 days of daily candles
                val calendar = java.util.Calendar.getInstance()
                val endDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
                
                calendar.add(java.util.Calendar.DAY_OF_MONTH, -30)
                val startDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(calendar.time)
                
                val response = apiService.getCandles(
                    ticker = ticker,
                    from = startDate,
                    till = endDate,
                    interval = 24 // Daily candles
                )
                candles = parseCandles(response)
                candlesErrorMessage = null
            } catch (e: Exception) {
                candlesErrorMessage = "Failed to load candles: ${e.message}"
            }
        }
    }

    private fun parseSecurity(response: JsonObject): Security? {
        val securities = response.getAsJsonObject("securities")
        val marketdata = response.getAsJsonObject("marketdata")

        if (securities != null && marketdata != null) {
            val secData = securities.getAsJsonArray("data")
            val marketData = marketdata.getAsJsonArray("data")

            if (secData.size() > 0 && marketData.size() > 0) {
                val secRow = secData[0].asJsonArray
                val secColumns = securities.getAsJsonArray("columns")
                val marketRow = marketData[0].asJsonArray
                val marketColumns = marketdata.getAsJsonArray("columns")

                fun safeGetString(row: JsonArray, columns: JsonArray, name: String): String {
                    val index = columns.indexOfs(name)
                    return if (index != -1 && !row[index].isJsonNull) row[index].asString else "N/A"
                }

                val shortName = safeGetString(secRow, secColumns, "SHORTNAME")
                val issueSize = safeGetString(secRow, secColumns, "ISSUESIZE")
                val capitalization = safeGetString(secRow, secColumns, "LOTSIZE")
                val openPrice = safeGetString(marketRow, marketColumns, "OPEN")
                val closePrice = safeGetString(secRow, secColumns, "PREVLEGALCLOSEPRICE")

                return Security(
                    shortName = shortName,
                    issueSize = issueSize,
                    capitalization = capitalization,
                    openPrice = openPrice,
                    closePrice = closePrice
                )
            }
        }
        return null
    }

    private fun parseCandles(response: JsonObject): List<Candle> {
        val candlesData = response.getAsJsonObject("candles")
        val data = candlesData?.getAsJsonArray("data")
        val columns = candlesData?.getAsJsonArray("columns")

        if (data != null && columns != null) {
            val candlesList = mutableListOf<Candle>()
            
            for (i in 0 until data.size()) {
                val row = data[i].asJsonArray
                
                fun safeGetDouble(row: JsonArray, columns: JsonArray, name: String): Double {
                    val index = columns.indexOfs(name)
                    return if (index != -1 && !row[index].isJsonNull) {
                        try {
                            row[index].asDouble
                        } catch (e: Exception) {
                            0.0
                        }
                    } else 0.0
                }
                
                fun safeGetString(row: JsonArray, columns: JsonArray, name: String): String {
                    val index = columns.indexOfs(name)
                    return if (index != -1 && !row[index].isJsonNull) row[index].asString else ""
                }

                val candle = Candle(
                    open = safeGetDouble(row, columns, "open"),
                    close = safeGetDouble(row, columns, "close"),
                    high = safeGetDouble(row, columns, "high"),
                    low = safeGetDouble(row, columns, "low"),
                    value = safeGetDouble(row, columns, "value"),
                    volume = safeGetDouble(row, columns, "volume"),
                    begin = safeGetString(row, columns, "begin"),
                    end = safeGetString(row, columns, "end")
                )
                
                candlesList.add(candle)
            }
            
            return candlesList
        }
        
        return emptyList()
    }
}

fun JsonArray.indexOfs(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}