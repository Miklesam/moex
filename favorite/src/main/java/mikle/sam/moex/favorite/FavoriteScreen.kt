package mikle.sam.moex.favorite

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteScreen(
    viewModel: FavoriteViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit
) {
    val stockState = viewModel.stockState

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        LazyColumn {
            items(stockState.size) { index ->
                FavoriteStockPriceDisplay(
                    stock = stockState[index],
                    onStockClick = onStockClick
                )
            }
        }
    }
}

@Composable
fun FavoriteStockPriceDisplay(stock: FavoriteStockState, onStockClick: (String) -> Unit) {
    val changeColor = when {
        stock.lastChange > 0 -> Color(0xFF4CAF50) // Приятный зеленый
        stock.lastChange < 0 -> Color(0xFFE53935) // Приятный красный
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

fun formatMarketValue(value: Double): String {
    val trillion = 1_000_000_000_000.0
    val billion = 1_000_000_000.0
    val million = 1_000_000.0

    return when {
        value.isNaN() -> "N/A"
        value >= trillion -> "%.2fT".format(value / trillion)
        value >= billion -> "%.2fB".format(value / billion)
        value >= million -> "%.2fM".format(value / million)
        else -> "%.2f".format(value)
    }
}
