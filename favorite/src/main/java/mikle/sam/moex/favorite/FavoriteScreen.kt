package mikle.sam.moex.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = hiltViewModel()) {
	val favorites = viewModel.favorites.collectAsState()
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.TopStart
	) {
		LazyColumn {
			items(favorites.value) { ticker ->
				Text(text = ticker)
			}
		}
	}
}


