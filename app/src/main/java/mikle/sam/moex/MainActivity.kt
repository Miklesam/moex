package mikle.sam.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import mikle.sam.moex.details.DetailScreen
import mikle.sam.moex.search.SearchScreen
import mikle.sam.moex.stock.StockScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) {
                BottomNavGraph(navController = navController)
            }
        }
    }
}

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Watchlist.route
    ) {
        composable(route = BottomBarScreen.Watchlist.route) {
            StockScreen(onStockClick = { stockName ->
                navController.navigate("details/$stockName")
            })
        }
        composable(route = BottomBarScreen.Search.route) {
            SearchScreen(
                onStockClick = { stockName ->
                    navController.navigate("details/$stockName")
                }
            )
        }
        composable("details/{stockName}") { backStackEntry ->
            DetailScreen(
                stockName = backStackEntry.arguments?.getString("stockName")
            )
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Watchlist,
        BottomBarScreen.Search,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        NavigationBar {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}