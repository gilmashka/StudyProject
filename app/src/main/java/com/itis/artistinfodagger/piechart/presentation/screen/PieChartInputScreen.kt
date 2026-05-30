package com.itis.artistinfodagger.piechart.presentation.screen

import com.itis.artistinfodagger.piechart.presentation.viewmodel.PieChartViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.itis.artistinfodagger.piechart.data.models.PieChartSegment


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChartInputScreen(
    viewModel: PieChartViewModel,
    onNavigateToDisplay: () -> Unit
) {
    val segments by viewModel.segments.collectAsState()
    val totalPercentage = viewModel.getTotalPercentage()

    var name by remember { mutableStateOf("") }
    var percentage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Секторы диаграммы",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(segments) { segment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = segment.name,
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "${segment.percentage}%",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = { viewModel.removeSegment(segment.id) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Удалить",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                Divider()
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Итого:",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "$totalPercentage% / 100%",
                    style = MaterialTheme.typography.titleMedium,
                    color = if (totalPercentage == 100)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Название") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = percentage,
            onValueChange = { percentage = it.filter { it.isDigit() } },
            label = { Text("Процент") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    val percent = percentage.toIntOrNull()
                    if (percent != null && percent in 1..100) {
                        viewModel.addSegment(name, percent)
                        name = ""
                        percentage = ""
                    }
                },
                modifier = Modifier.weight(1f),
                enabled = name.isNotBlank() && percentage.isNotBlank()
            ) {
                Text("Добавить")
            }

            Button(
                onClick = {
                    viewModel.generateChart()
                    onNavigateToDisplay()
                },
                modifier = Modifier.weight(1f),
                enabled = segments.isNotEmpty() && totalPercentage == 100
            ) {
                Text("Показать")
            }
        }

        if (segments.isNotEmpty() && totalPercentage != 100) {
            Text(
                text = "Нужно 100% (сейчас $totalPercentage%)",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
@Composable
fun SegmentInputItem(
    segment: PieChartSegment,
    onRemove: () -> Unit,
    onPercentageChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = segment.name,
                modifier = Modifier.weight(1f)
            )

            var percentageText by remember(segment.percentage) {
                mutableStateOf(segment.percentage.toString())
            }

            OutlinedTextField(
                value = percentageText,
                onValueChange = {
                    percentageText = it.filter { char -> char.isDigit() }
                    val percent = percentageText.toIntOrNull()
                    if (percent != null && percent in 1..100) {
                        onPercentageChange(percent)
                    }
                },
                label = { Text("%") },
                modifier = Modifier.width(80.dp),
                singleLine = true
            )

            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}