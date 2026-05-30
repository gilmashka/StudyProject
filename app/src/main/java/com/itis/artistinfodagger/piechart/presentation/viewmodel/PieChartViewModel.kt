package com.itis.artistinfodagger.piechart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.itis.artistinfodagger.piechart.data.models.PieChartData
import com.itis.artistinfodagger.piechart.data.models.PieChartSegment
import kotlin.collections.filter
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PieChartViewModel : ViewModel() {

    private val _segments = MutableStateFlow<List<PieChartSegment>>(emptyList())
    val segments: StateFlow<List<PieChartSegment>> = _segments.asStateFlow()

    private val _pieChartData = MutableStateFlow<PieChartData?>(null)
    val pieChartData: StateFlow<PieChartData?> = _pieChartData.asStateFlow()

    private var nextId = 1

    fun addSegment(name: String, percentage: Int) {
        if (name.isBlank()) return
        if (percentage <= 0 || percentage > 100) return

        val currentTotal = _segments.value.sumOf { it.percentage }
        if (currentTotal + percentage > 100) return

        _segments.update { current ->
            current + PieChartSegment(
                id = nextId++,
                name = name,
                percentage = percentage
            )
        }
    }

    fun removeSegment(id: Int) {
        _segments.update { current ->
            current.filter { it.id != id }
        }
    }

    fun updateSegment(id: Int, newPercentage: Int) {
        _segments.update { current ->
            current.map { segment ->
                if (segment.id == id) {
                    segment.copy(percentage = newPercentage)
                } else {
                    segment
                }
            }
        }
    }

    fun generateChart() {
        val currentSegments = _segments.value
        val total = currentSegments.sumOf { it.percentage }

        if (currentSegments.isNotEmpty() && total == 100) {
            _pieChartData.value = PieChartData(currentSegments)
        }
    }

    fun getTotalPercentage(): Int = _segments.value.sumOf { it.percentage }
}