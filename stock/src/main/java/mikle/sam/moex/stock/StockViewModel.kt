package mikle.sam.moex.stock

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.provideRetrofit
import java.util.concurrent.ConcurrentHashMap

data class StockScreenState(
    val ticker: String,
    val price: Double,
    val lastChange: Double,
    val lastChangePrcnt: Double,
    val issueCapitalization: Double
)

class StockViewModel(
    private val apiService: MoexApiService
) : ViewModel() {
    var stockState by mutableStateOf<List<StockScreenState>>(emptyList())
        private set

    init {
        fetchStockPrice(listOf("LKOH", "GAZP","NVTK","MAGN","CHMF","ARSA"))
    }

    private fun fetchStockPrice(tickers: List<String>) {
        val prices = ConcurrentHashMap<String, StockScreenState>()
        viewModelScope.launch(Dispatchers.IO) {
            tickers.forEach { ticker ->
                launch {
                    val result = apiService.getStockData(ticker)
                    val stockData = parseStockDataFromResponse(result)
                    if (stockData != null) {
                        prices[ticker] = stockData
                        stockState = tickers.mapNotNull { t -> prices[t] }
                    }
                }
            }
        }
    }

    fun parseStockDataFromResponse(response: JsonObject): StockScreenState? {
        val marketdata = response.getAsJsonObject("marketdata")
        val dataArray = marketdata.getAsJsonArray("data")

        if (dataArray.size() > 0) {
            val data = dataArray[0].asJsonArray
            val columns = marketdata.getAsJsonArray("columns")
            val lastPriceIndex = columns.indexOf("LAST")
            val lastChangeIndex = columns.indexOf("LASTCHANGE")
            val lastChangePrcntIndex = columns.indexOf("LASTCHANGEPRCNT")
            val issueCapitalizationIndex = columns.indexOf("ISSUECAPITALIZATION")
            val secIdIndex = columns.indexOf("SECID")

            if (lastPriceIndex != -1 && lastChangeIndex != -1 && lastChangePrcntIndex != -1 && issueCapitalizationIndex != -1 && secIdIndex != -1) {
                return StockScreenState(
                    ticker = data[secIdIndex].asString,
                    price = data[lastPriceIndex].asDouble,
                    lastChange = data[lastChangeIndex].asDouble,
                    lastChangePrcnt = data[lastChangePrcntIndex].asDouble,
                    issueCapitalization = data[issueCapitalizationIndex].asDouble
                )
            }
        }
        return null
    }
}

fun JsonArray.indexOf(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}
