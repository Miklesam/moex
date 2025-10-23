package mikle.sam.moex.network

import com.google.gson.JsonObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoexApiService {
    @GET("iss/engines/stock/markets/shares/boards/TQBR/securities/{ticker}.json")
    suspend fun getStockData(@Path("ticker") ticker: String): JsonObject

    @GET("iss/engines/stock/markets/shares/securities/{ticker}.json")
    suspend fun getSecurity(@Path("ticker") ticker: String): JsonObject

    @GET("iss/securities.json")
    suspend fun search(@Query("q") query: String): JsonObject

    @GET("iss/engines/stock/markets/shares/boards/tqbr/securities/{ticker}/candles.json")
    suspend fun getCandles(
        @Path("ticker") ticker: String,
        @Query("from") from: String? = null,
        @Query("till") till: String? = null,
        @Query("interval") interval: Int = 24
    ): JsonObject
}

fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://iss.moex.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}