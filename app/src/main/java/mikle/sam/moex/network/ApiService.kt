package mikle.sam.moex.network

import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface MoexApiService {
    @GET("iss/engines/stock/markets/shares/boards/TQBR/securities/{ticker}.json")
    suspend fun getStockData(@Path("ticker") ticker: String): JsonObject

    @GET("iss/engines/stock/markets/shares/securities/{ticker}.json")
    suspend fun getSecurity(@Path("ticker") ticker: String): JsonObject

}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://iss.moex.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}