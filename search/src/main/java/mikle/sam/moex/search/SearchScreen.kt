package mikle.sam.moex.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
    onStockClick: (String) -> Unit
) {
    val searchState = searchViewModel.searchState
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        OutlinedTextField(
            value = searchState.query,
            onValueChange = { searchViewModel.onQueryChanged(it) },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { searchViewModel.onSearch() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }
        LazyColumn {
            items(searchState.results) {
                Text(
                    text = it.toString(),
                    modifier = Modifier.clickable {
                        onStockClick(it.toString())
                    }
                )
            }
        }
    }
}