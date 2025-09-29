package mikle.sam.moex

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.provideRetrofit
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.measureTime

class StockViewModel : ViewModel() {
    private val apiService: MoexApiService = provideRetrofit().create(MoexApiService::class.java)
    var stockPrice by mutableStateOf<List<Pair<String, Double>>>(emptyList())
        private set

    init {
        val time: Duration = measureTime {
            fetchStockPrice(listOf("LKOH", "GAZP","NVTK","MAGN","CHMF","ARSA","FEES","ENPG"))
        }
        Log.w("TimeRequest", time.toString())
    }

    private fun fetchStockPrice(ticker: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val res: List<Pair<String, Double>> = ticker.map { ticker ->
                async {
                    val result = apiService.getStockData(ticker)
                    Pair(ticker, getPriceFromResponce(result))
                }
            }.awaitAll()
            stockPrice = res
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
