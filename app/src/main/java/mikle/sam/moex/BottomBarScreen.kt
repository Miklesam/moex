package mikle.sam.moex

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Watchlist : BottomBarScreen(
        route = "watchlist",
        title = "Watchlist",
        icon = Icons.Default.List
    )

    object Search : BottomBarScreen(
        route = "search",
        title = "Search",
        icon = Icons.Default.Search
    )
}