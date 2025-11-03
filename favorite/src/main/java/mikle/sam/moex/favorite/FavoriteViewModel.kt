package mikle.sam.moex.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import mikle.sam.moex.network.MoexApiService
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

data class FavoriteStockState(
    val ticker: String,
    val price: Double,
    val lastChange: Double,
    val lastChangePrcnt: Double,
    val issueCapitalization: Double
)

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val repository: FavoriteRepository,
    private val apiService: MoexApiService
) : ViewModel() {
    var stockState by mutableStateOf<List<FavoriteStockState>>(emptyList())
        private set

    private val favoriteTickers: StateFlow<List<String>> = repository
        .favorites()
        .map { list -> list.map { it.ticker } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch {
            favoriteTickers.collect { tickers ->
                if (tickers.isNotEmpty()) {
                    fetchStockPrice(tickers)
                } else {
                    stockState = emptyList()
                }
            }
        }
    }

    private fun fetchStockPrice(tickers: List<String>) {
        val prices = ConcurrentHashMap<String, FavoriteStockState>()
        viewModelScope.launch(Dispatchers.IO) {
            tickers.forEach { ticker ->
                launch {
                    try {
                        val result = apiService.getStockData(ticker)
                        val stockData = parseStockDataFromResponse(result, ticker)
                        if (stockData != null) {
                            prices[ticker] = stockData
                            // Update state as each stock is loaded
                            stockState = tickers.mapNotNull { t -> prices[t] }
                        }
                    } catch (e: Exception) {
                        // Ignore errors for individual stocks
                    }
                }
            }
        }
    }

    private fun parseStockDataFromResponse(response: JsonObject, ticker: String): FavoriteStockState? {
        val marketdata = response.getAsJsonObject("marketdata")
        val dataArray = marketdata.getAsJsonArray("data")

        if (dataArray.size() > 0) {
            val data = dataArray[0].asJsonArray
            val columns = marketdata.getAsJsonArray("columns")
            val lastPriceIndex = columns.indexOf("LAST")
            val lastChangeIndex = columns.indexOf("LASTCHANGE")
            val lastChangePrcntIndex = columns.indexOf("LASTCHANGEPRCNT")
            val issueCapitalizationIndex = columns.indexOf("ISSUECAPITALIZATION")

            if (lastPriceIndex != -1 && lastChangeIndex != -1 && lastChangePrcntIndex != -1 && issueCapitalizationIndex != -1) {
                return FavoriteStockState(
                    ticker = ticker,
                    price = data[lastPriceIndex].asDouble,
                    lastChange = data[lastChangeIndex].asDouble,
                    lastChangePrcnt = data[lastChangePrcntIndex].asDouble,
                    issueCapitalization = data[issueCapitalizationIndex].asDouble
                )
            }
        }
        return null
    }

    private fun JsonArray.indexOf(value: String): Int {
        for (i in 0 until this.size()) {
            if (this[i].asString == value) return i
        }
        return -1
    }

    fun addFavorite(ticker: String) {
        viewModelScope.launch {
            repository.addFavorite(ticker)
        }
    }
}


