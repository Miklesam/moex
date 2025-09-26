package mikle.sam.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.gson.JsonArray

class MainActivity : ComponentActivity() {

    private val stockViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp(stockViewModel)
        }
    }
}


@Composable
fun MyApp(stockViewModel: StockViewModel) {
    val stockPrice = stockViewModel.stockPrice

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn {
            items(stockPrice.size) { stock ->
                StockPriceDisplay(
                    name = stockPrice[stock].first,
                    stockPrice = stockPrice[stock].second
                )
            }
        }
    }

}

@Composable
fun StockPriceDisplay(name: String, stockPrice: Double?) {
    Text(
        text = if (stockPrice != null) {
            "Текущая цена акции $name: $stockPrice RUB"
        } else {
            "Не удалось получить данные"
        }
    )
}


fun JsonArray.indexOf(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}