package mikle.sam.moex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mikle.sam.moex.details.DetailViewModel
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.provideRetrofit
import mikle.sam.moex.stock.StockViewModel

object ViewModelFactory : ViewModelProvider.Factory {

    private val apiService by lazy { provideRetrofit().create(MoexApiService::class.java) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(StockViewModel::class.java) -> {
                StockViewModel(apiService) as T
            }

            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(apiService) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}