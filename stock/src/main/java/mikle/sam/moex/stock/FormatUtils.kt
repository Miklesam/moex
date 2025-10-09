package mikle.sam.moex.stock

fun formatMarketValue(value: Double): String {
    val trillion = 1_000_000_000_000.0
    val billion = 1_000_000_000.0
    val million = 1_000_000.0

    return when {
        value.isNaN() -> "N/A"
        value >= trillion -> "%.2fT".format(value / trillion)
        value >= billion -> "%.2fB".format(value / billion)
        value >= million -> "%.2fM".format(value / million)
        else -> "%.2f".format(value)
    }
}