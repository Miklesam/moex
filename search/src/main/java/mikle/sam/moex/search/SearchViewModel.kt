package mikle.sam.moex.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.Search
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val moexApiService: MoexApiService
) : ViewModel() {

    var searchState by mutableStateOf(SearchState())
        private set

    fun onQueryChanged(query: String) {
        searchState = searchState.copy(query = query)
    }

    fun onSearch() {
        viewModelScope.launch {
            val response = moexApiService.search(searchState.query)
            val search = Gson().fromJson(response, Search::class.java)
            val result = search.securities.data.map {
                it[0]
            }
            searchState = searchState.copy(results = result)
        }
    }
}

data class SearchState(
    val query: String = "",
    val results: List<Any> = emptyList()
)