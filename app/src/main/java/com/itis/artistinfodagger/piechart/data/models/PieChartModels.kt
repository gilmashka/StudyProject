package com.itis.artistinfodagger.piechart.data.models
data class PieChartSegment(
    val id: Int,
    val name: String,
    val percentage: Int
)

data class PieChartData(
    val segments: List<PieChartSegment>
) {
    init {
        val total = segments.sumOf { it.percentage }
        require(total == 100) { "Total percentage must be 100, but got $total" }
    }
}