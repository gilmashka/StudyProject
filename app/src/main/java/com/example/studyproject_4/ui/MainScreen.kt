package com.example.studyproject_4.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studyproject_4.R
import com.example.studyproject_4.ui.viewmodel.DispatcherType
import com.example.studyproject_4.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val coroutinesCount by viewModel.coroutinesCount.collectAsState()
    val selectedDispatcher by viewModel.selectedDispatcher.collectAsState()
    val isSequential by viewModel.isSequential.collectAsState()
    val isParallel by viewModel.isParallel.collectAsState()
    val isDeferred by viewModel.isDeferred.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.resetSettings.collect {
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(R.string.coroutines_count_slider),
                style = MaterialTheme.typography.titleMedium
            )

            Slider(
                value = coroutinesCount,
                onValueChange = viewModel::updateCoroutinesCount,
                valueRange = 10f..100f,
                steps = 17,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "${coroutinesCount.toInt()}",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = stringResource(R.string.dispatcher_dropdown),
                style = MaterialTheme.typography.titleMedium
            )

            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedDispatcher.toDisplayName(),
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DispatcherType.values().forEach { dispatcher ->
                        DropdownMenuItem(
                            text = { Text(text = dispatcher.toDisplayName()) },
                            onClick = {
                                viewModel.updateDispatcher(dispatcher)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.sequential_switch))
                    Switch(
                        checked = isSequential,
                        onCheckedChange = viewModel::updateSequential
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.parallel_switch))
                    Switch(
                        checked = isParallel,
                        onCheckedChange = viewModel::updateParallel
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.deferred_switch))
                    Switch(
                        checked = isDeferred,
                        onCheckedChange = viewModel::updateDeferred
                    )
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.loading))
            } else {
                Button(
                    onClick = viewModel::startCoroutines,
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = stringResource(R.string.start_button))
                }
            }
        }
    }
}