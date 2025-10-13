package mikle.sam.moex.details

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailScreen(
    stockName: String?,
    detailViewModel: DetailViewModel = hiltViewModel()
) {
    LaunchedEffect(stockName) {
        stockName?.let { detailViewModel.fetchSecurity(it) }
    }
    val security = detailViewModel.security
    val errorMessage = detailViewModel.errorMessage

    when {
        errorMessage != null -> {
            Text(text = errorMessage)
        }
        security != null -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Short Name: ${security.shortName}")
                Text(text = "Issue Size: ${security.issueSize}")
                Text(text = "Capitalization: ${security.capitalization}")
                Text(text = "Open Price: ${security.openPrice}")
                Text(text = "Close Price: ${security.closePrice}")
            }
        }
        else -> {
            Text(text = "Loading...")
        }
    }
}