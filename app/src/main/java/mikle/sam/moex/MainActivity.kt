package mikle.sam.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.JsonArray

class MainActivity : ComponentActivity() {

    private val stockViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MyApp(stockViewModel, onStockClick = { stockName ->
                        navController.navigate("details/$stockName")
                    })
                }
                composable("details/{stockName}") { backStackEntry ->
                    DetailScreen(
                        stockName = backStackEntry.arguments?.getString("stockName"),
                        detailViewModel = viewModel()
                    )
                }
            }
        }
    }
}


@Composable
fun MyApp(stockViewModel: StockViewModel, onStockClick: (String) -> Unit) {
    val stockPrice = stockViewModel.stockPrice

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn {
            items(stockPrice.size) { stock ->
                StockPriceDisplay(
                    name = stockPrice[stock].first,
                    stockPrice = stockPrice[stock].second,
                    onStockClick = onStockClick
                )
            }
        }
    }

}

@Composable
fun StockPriceDisplay(name: String, stockPrice: Double?, onStockClick: (String) -> Unit) {
    Text(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onStockClick(name) },
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