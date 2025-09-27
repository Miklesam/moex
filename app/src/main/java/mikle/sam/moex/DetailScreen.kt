package mikle.sam.moex

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(stockName: String?, detailViewModel: DetailViewModel = viewModel()) {
    LaunchedEffect(stockName) {
        stockName?.let { detailViewModel.fetchSecurity(it) }
    }
    val security = detailViewModel.security
    if (security != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "SECID: ${security.secId}")
            Text(text = "Short Name: ${security.shortName}")
            Text(text = "Reg Number: ${security.regNumber}")
            Text(text = "ISIN: ${security.isin}")
            Text(text = "Is Traded: ${if (security.isTraded) "Yes" else "No"}")
            Text(text = "Type: ${security.type}")
        }
    } else {
        Text(text = "Loading...")
    }
}