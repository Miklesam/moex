package mikle.sam.moex.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StockScreen(stockViewModel: StockViewModel, onStockClick: (String) -> Unit) {
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
            "$name: $stockPrice RUB"
        } else {
            "Не удалось получить данные"
        }
    )
}