package com.itis.artistinfodagger.piechart.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import com.itis.artistinfodagger.piechart.component.PieChartView
import com.itis.artistinfodagger.piechart.presentation.viewmodel.PieChartViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChartDisplayScreen(
    viewModel: PieChartViewModel,
    onBack: () -> Unit
) {
    val pieChartData by viewModel.pieChartData.collectAsState()
    var selectedSegmentIndex by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Круговая диаграмма") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            pieChartData?.let { data ->
                PieChartView(
                    data = data,
                    selectedIndex = selectedSegmentIndex,
                    onSegmentSelected = { index ->
                        selectedSegmentIndex = if (selectedSegmentIndex == index) null else index
                    },
                    modifier = Modifier.size(220.dp)
                )
            } ?: run {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Нет данных для отображения",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Button(onClick = onBack) {
                        Text("Вернуться к вводу")
                    }
                }
            }
        }
    }
}