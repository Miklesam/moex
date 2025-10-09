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
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.provideRetrofit
import mikle.sam.moex.network.Security

class DetailViewModel(
    private val apiService: MoexApiService
) : ViewModel() {
    var security by mutableStateOf<Security?>(null)
        private set
    var errorMessage by mutableStateOf<String?>(null)
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
}

fun JsonArray.indexOfs(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}