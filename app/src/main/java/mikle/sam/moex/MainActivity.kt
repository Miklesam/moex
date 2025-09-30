package mikle.sam.moex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import mikle.sam.moex.details.DetailScreen
import mikle.sam.moex.stock.StockScreen
import mikle.sam.moex.stock.StockViewModel

class MainActivity : ComponentActivity() {

    private val stockViewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    StockScreen(stockViewModel, onStockClick = { stockName ->
                        navController.navigate("details/$stockName")
                    })
                }
                composable("details/{stockName}") { backStackEntry ->
                    DetailScreen(
                        stockName = backStackEntry.arguments?.getString("stockName"),
                        detailViewModel = viewModel()
                    )
                }
            }
        }
    }
}