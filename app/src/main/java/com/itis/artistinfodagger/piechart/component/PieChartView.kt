package com.itis.artistinfodagger.piechart.component

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.*
import androidx.compose.ui.input.pointer.PointerEventType
import com.itis.artistinfodagger.piechart.data.models.PieChartData

@Composable
fun PieChartView(
    data: PieChartData,
    selectedIndex: Int?,
    onSegmentSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFF9C27B0),
        Color(0xFFF44336),
        Color(0xFF00BCD4),
        Color(0xFFFFEB3B),
        Color(0xFF795548),
        Color(0xFF607D8B),
        Color(0xFFE91E63)
    )

    Box(
        modifier = modifier
            .size(350.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val position = event.changes.first().position

                        when (event.type) {
                            PointerEventType.Press -> {
                                if (canvasSize == Size.Zero) return@awaitPointerEventScope

                                val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
                                val radius = min(canvasSize.width, canvasSize.height) / 2

                                val dx = position.x - center.x
                                val dy = position.y - center.y
                                val distance = sqrt(dx * dx + dy * dy)

                                if (distance <= radius) {
                                    var angle = atan2(dy, dx).toDouble()
                                    angle = if (angle < 0) angle + 2 * PI else angle

                                    var currentAngle = -PI / 2
                                    var foundIndex = -1

                                    for (i in data.segments.indices) {
                                        val sweepAngle = data.segments[i].percentage / 100.0 * 2 * PI
                                        val endAngle = currentAngle + sweepAngle

                                        if (angle >= currentAngle && angle < endAngle) {
                                            foundIndex = i
                                            break
                                        }
                                        currentAngle = endAngle
                                    }

                                    if (foundIndex != -1) {
                                        onSegmentSelected(foundIndex)
                                    } else {
                                        onSegmentSelected(-1)
                                    }
                                } else {
                                    onSegmentSelected(-1)
                                }
                            }                            else -> {}
                        }
                    }
                }
            }
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            canvasSize = this.size
            val center = Offset(canvasSize.width / 2, canvasSize.height / 2)
            val outerRadius = min(canvasSize.width, canvasSize.height) / 2


            val gapAngle = 8f

            var startAngle = -90f

            data.segments.forEachIndexed { index, segment ->
                val fullSweep = (segment.percentage / 100f) * 360f
                val sectorSweep = fullSweep - gapAngle

                val strokeWidth = if (selectedIndex == index) {
                    outerRadius * 0.45f
                } else {
                    outerRadius * 0.35f
                }

                val paintColor = if (selectedIndex == index) {
                    colors[index % colors.size].copy(alpha = 0.6f)
                } else {
                    colors[index % colors.size]
                }

                drawArc(
                    color = Color.White,
                    startAngle = startAngle,
                    sweepAngle = gapAngle,
                    useCenter = false,
                    topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth)
                )

                drawArc(
                    color = paintColor,
                    startAngle = startAngle + gapAngle,
                    sweepAngle = sectorSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth)
                )

                val midAngle = startAngle + gapAngle + sectorSweep / 2
                val radians = Math.toRadians(midAngle.toDouble())
                val textRadius = outerRadius - strokeWidth / 2
                val textX = center.x + (textRadius * cos(radians)).toFloat()
                val textY = center.y + (textRadius * sin(radians)).toFloat()

                drawIntoCanvas { canvas ->
                    canvas.nativeCanvas.apply {
                        val paint = Paint().apply {
                            color = android.graphics.Color.WHITE
                            textSize = if (selectedIndex == index) 32f else 28f
                            textAlign = Paint.Align.CENTER
                            isFakeBoldText = true
                            setShadowLayer(2f, 1f, 1f, android.graphics.Color.BLACK)
                        }
                        canvas.nativeCanvas.drawText("${segment.percentage}%", textX, textY + 8, paint)
                    }
                }

                startAngle += fullSweep
            }
        }
    }
}