package mikle.sam.moex.network

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("securities")
    val securities: Securities
)

data class Securities(
    @SerializedName("columns")
    val columns: List<String>,
    @SerializedName("data")
    val data: List<List<Any>>
)