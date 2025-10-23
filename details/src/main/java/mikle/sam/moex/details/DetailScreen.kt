package mikle.sam.moex.details

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    stockName: String?,
    detailViewModel: DetailViewModel = hiltViewModel(),
    onAddFavorite: (String) -> Unit
) {
    LaunchedEffect(stockName) {
        stockName?.let { 
            detailViewModel.fetchSecurity(it)
            detailViewModel.fetchCandles(it)
        }
    }
    
    val security = detailViewModel.security
    val errorMessage = detailViewModel.errorMessage
    val candles = detailViewModel.candles
    val candlesErrorMessage = detailViewModel.candlesErrorMessage

    val snackbarHostState = androidx.compose.runtime.remember { SnackbarHostState() }
    val scope = androidx.compose.runtime.rememberCoroutineScope()
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                stockName?.let {
                    onAddFavorite(it)
                    scope.launch {
                        snackbarHostState.showSnackbar("Added to favorites: $it")
                    }
                }
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add to favorites")
            }
        }
    ) { innerPadding ->
        when {
            errorMessage != null -> {
                Text(text = errorMessage, modifier = Modifier.padding(innerPadding))
            }
            security != null -> {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(text = "Short Name: ${security.shortName}")
                    Text(text = "Issue Size: ${security.issueSize}")
                    Text(text = "Capitalization: ${security.capitalization}")
                    Text(text = "Open Price: ${security.openPrice}")
                    Text(text = "Close Price: ${security.closePrice}")
                    
                    // Price Chart Section
                    Text(
                        text = "Price Chart (Last 30 Days)",
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    
                    if (candlesErrorMessage != null) {
                        Text(
                            text = "Chart Error: $candlesErrorMessage",
                            color = androidx.compose.ui.graphics.Color.Red,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }
                    
                    PriceChart(
                        candles = candles,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                    )
                }
            }
            else -> {
                Text(text = "Loading...", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}