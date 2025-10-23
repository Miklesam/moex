package mikle.sam.moex.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import mikle.sam.moex.network.Candle
import kotlin.math.max
import kotlin.math.min
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PriceChart(
    candles: List<Candle>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        if (candles.isEmpty()) {
            Text(
                text = "No chart data available",
                modifier = Modifier.padding(16.dp),
                fontSize = 14.sp
            )
        } else {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                drawPriceChart(candles)
            }
        }
    }
}

private fun DrawScope.drawPriceChart(candles: List<Candle>) {
    if (candles.isEmpty()) return

    val padding = 60f // Increased padding for x-axis labels
    val chartWidth = size.width - 2 * padding
    val chartHeight = size.height - 2 * padding
    
    // Find min and max prices for scaling
    val prices = candles.map { it.close }
    val minPrice = prices.minOrNull() ?: 0.0
    val maxPrice = prices.maxOrNull() ?: 0.0
    val priceRange = maxPrice - minPrice
    
    if (priceRange <= 0) return

    // Create path for the line chart
    val path = Path()
    val stepX = chartWidth / (candles.size - 1)
    
    candles.forEachIndexed { index, candle ->
        val x = padding + index * stepX
        val y = padding + chartHeight - ((candle.close - minPrice) / priceRange * chartHeight).toFloat()
        
        if (index == 0) {
            path.moveTo(x, y)
        } else {
            path.lineTo(x, y)
        }
    }

    // Draw the line
    drawPath(
        path = path,
        color = Color.Blue,
        style = Stroke(width = 3f)
    )

    // Draw data points
    candles.forEachIndexed { index, candle ->
        val x = padding + index * stepX
        val y = padding + chartHeight - ((candle.close - minPrice) / priceRange * chartHeight).toFloat()
        
        drawCircle(
            color = Color.Blue,
            radius = 4f,
            center = Offset(x, y)
        )
    }

    // Draw price labels on y-axis
    val priceStep = priceRange / 5
    for (i in 0..5) {
        val price = minPrice + i * priceStep
        val y = padding + chartHeight - (i * chartHeight / 5)
        
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "%.2f".format(price),
                padding - 5f,
                y + 5f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 12f
                }
            )
        }
    }

    // Draw date labels on x-axis
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault()) // e.g., "Oct 15"
    val stepCount = minOf(5, candles.size - 1) // Show max 5 date labels
    val stepInterval = maxOf(1, candles.size / stepCount)
    
    for (i in 0 until candles.size step stepInterval) {
        val candle = candles[i]
        val x = padding + i * stepX
        
        try {
            // Try multiple date formats that MOEX might use
            val dateFormats = listOf(
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss'Z'",
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd"
            )
            
            var parsedDate: Date? = null
            for (format in dateFormats) {
                try {
                    parsedDate = SimpleDateFormat(format, Locale.getDefault()).parse(candle.begin)
                    break
                } catch (e: Exception) {
                    // Try next format
                }
            }
            
            val formattedDate = if (parsedDate != null) {
                dateFormat.format(parsedDate)
            } else {
                // Fallback: try to extract date parts manually
                val dateParts = candle.begin.split("T")[0].split("-")
                if (dateParts.size >= 3) {
                    val month = when (dateParts[1]) {
                        "01" -> "Jan"
                        "02" -> "Feb"
                        "03" -> "Mar"
                        "04" -> "Apr"
                        "05" -> "May"
                        "06" -> "Jun"
                        "07" -> "Jul"
                        "08" -> "Aug"
                        "09" -> "Sep"
                        "10" -> "Oct"
                        "11" -> "Nov"
                        "12" -> "Dec"
                        else -> dateParts[1]
                    }
                    val day = dateParts[2].toIntOrNull() ?: 1
                    "$month $day"
                } else {
                    "Day ${i + 1}"
                }
            }
            
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    formattedDate,
                    x - 20f, // Center the text
                    size.height - 10f, // Position at bottom
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        } catch (e: Exception) {
            // If all parsing fails, show day number
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "Day ${i + 1}",
                    x - 20f,
                    size.height - 10f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.BLACK
                        textSize = 10f
                        textAlign = android.graphics.Paint.Align.CENTER
                    }
                )
            }
        }
    }
}
