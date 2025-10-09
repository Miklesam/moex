package mikle.sam.moex.stock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StockScreen(stockViewModel: StockViewModel, onStockClick: (String) -> Unit) {
    val stockState = stockViewModel.stockState

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn {
            items(stockState.size) { stock ->
                StockPriceDisplay(
                    stock = stockState[stock],
                    onStockClick = onStockClick
                )
            }
        }
    }
}

@Composable
fun StockPriceDisplay(stock: StockScreenState, onStockClick: (String) -> Unit) {
    val changeColor = when {
        stock.lastChange > 0 -> Color.Green
        stock.lastChange < 0 -> Color.Red
        else -> Color.Black
    }
    Column(
        modifier = Modifier
            .padding(4.dp)
            .clickable { onStockClick(stock.ticker) },
    ) {
        Text(
            text = "${stock.ticker}: ${stock.price} RUB"
        )
        Text(
            text = "Change: ${stock.lastChange} (${stock.lastChangePrcnt}%)",
            color = changeColor
        )
        Text(
            text = "Capitalization: ${formatMarketValue(stock.issueCapitalization)}"
        )
    }
}