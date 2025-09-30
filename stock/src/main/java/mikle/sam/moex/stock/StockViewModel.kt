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

class StockViewModel : ViewModel() {
    private val apiService: MoexApiService = provideRetrofit().create(MoexApiService::class.java)
    var stockPrice by mutableStateOf<List<Pair<String, Double>>>(emptyList())
        private set

    init {
        fetchStockPrice(listOf("LKOH", "GAZP","NVTK","MAGN","CHMF","ARSA"))
    }

    private fun fetchStockPrice(tickers: List<String>) {
        val prices = ConcurrentHashMap<String, Double>()
        viewModelScope.launch(Dispatchers.IO) {
            tickers.forEach { ticker ->
                launch {
                    val result = apiService.getStockData(ticker)
                    val price = getPriceFromResponce(result)
                    prices[ticker] = price
                    stockPrice = tickers.map { t -> t to (prices[t] ?: 0.0) }
                }
            }
        }
    }

    fun getPriceFromResponce(response: JsonObject): Double {
        val marketdata = response.getAsJsonObject("marketdata")
        val dataArray = marketdata.getAsJsonArray("data")

        if (dataArray.size() > 0) {
            val data = dataArray[0].asJsonArray
            val columns = marketdata.getAsJsonArray("columns")
            val lastPriceIndex = columns.indexOf("LAST")
            if (lastPriceIndex != -1) {
                return data[lastPriceIndex].asDouble
            }
        }
        return 0.0
    }
}

fun JsonArray.indexOf(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}
