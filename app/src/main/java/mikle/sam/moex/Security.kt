package mikle.sam.moex

data class Security(
    val secId: String,
    val shortName: String,
    val regNumber: String,
    val isin: String,
    val isTraded: Boolean,
    val type: String
)