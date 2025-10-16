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
import androidx.compose.foundation.layout.padding
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
        stockName?.let { detailViewModel.fetchSecurity(it) }
    }
    val security = detailViewModel.security
    val errorMessage = detailViewModel.errorMessage

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
                Box(modifier = Modifier.padding(16.dp).padding(innerPadding)) {
                    Column(modifier = Modifier.align(Alignment.TopStart)) {
                        Text(text = "Short Name: ${security.shortName}")
                        Text(text = "Issue Size: ${security.issueSize}")
                        Text(text = "Capitalization: ${security.capitalization}")
                        Text(text = "Open Price: ${security.openPrice}")
                        Text(text = "Close Price: ${security.closePrice}")
                    }
                }
            }
            else -> {
                Text(text = "Loading...", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}