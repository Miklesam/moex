package mikle.sam.moex.network

import com.google.gson.annotations.SerializedName

data class Candle(
    val open: Double,
    val close: Double,
    val high: Double,
    val low: Double,
    val value: Double,
    val volume: Double,
    val begin: String,
    val end: String
)

data class CandlesResponse(
    @SerializedName("candles")
    val candles: CandlesData
)

data class CandlesData(
    @SerializedName("metadata")
    val metadata: Map<String, Any>,
    @SerializedName("columns")
    val columns: List<String>,
    @SerializedName("data")
    val data: List<List<Any>>
)
