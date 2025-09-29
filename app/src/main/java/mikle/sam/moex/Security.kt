package mikle.sam.moex

data class Security(
    val shortName: String,
    val issueSize: String,
    val capitalization: String,
    val openPrice: String,
    val closePrice: String
)