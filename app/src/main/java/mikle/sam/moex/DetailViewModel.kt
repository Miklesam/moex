package mikle.sam.moex

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import com.google.gson.JsonArray
import kotlinx.coroutines.launch
import mikle.sam.moex.network.MoexApiService
import mikle.sam.moex.network.provideRetrofit

class DetailViewModel : ViewModel() {
    private val apiService: MoexApiService = provideRetrofit().create(MoexApiService::class.java)
    var security by mutableStateOf<Security?>(null)
        private set

    fun fetchSecurity(ticker: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiService.getSecurity(ticker)
            security = parseSecurity(response)
        }
    }

    private fun parseSecurity(response: JsonObject): Security? {
        val securities = response.getAsJsonObject("securities")
        if (securities != null) {
            val data = securities.getAsJsonArray("data")
            if (data.size() > 0) {
            val row = data[0].asJsonArray
            val columns = securities.getAsJsonArray("columns")
            val secIdIndex = columns.indexOfs("SECID")
            val shortNameIndex = columns.indexOfs("SHORTNAME")
            val regNumberIndex = columns.indexOfs("REGNUMBER")
            val isinIndex = columns.indexOfs("ISIN")
            val typeIndex = columns.indexOfs("SECTYPE")

            return Security(
                secId = row[secIdIndex].asString,
                shortName = row[shortNameIndex].asString,
                regNumber = row[regNumberIndex].asString,
                isin = row[isinIndex].asString,
                isTraded = true,
                type = row[typeIndex].asString
            )
        }}
        return null
    }
}

fun JsonArray.indexOfs(value: String): Int {
    for (i in 0 until this.size()) {
        if (this[i].asString == value) return i
    }
    return -1
}