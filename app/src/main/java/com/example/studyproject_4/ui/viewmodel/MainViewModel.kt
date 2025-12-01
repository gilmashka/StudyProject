package com.example.studyproject_4.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

class MainViewModel : ViewModel() {
    private val _coroutinesCount = MutableStateFlow(50f)
    val coroutinesCount: StateFlow<Float> = _coroutinesCount.asStateFlow()

    private val _selectedDispatcher = MutableStateFlow(DispatcherType.DEFAULT)
    val selectedDispatcher: StateFlow<DispatcherType> = _selectedDispatcher.asStateFlow()

    private val _isSequential = MutableStateFlow(true)
    val isSequential: StateFlow<Boolean> = _isSequential.asStateFlow()

    private val _isParallel = MutableStateFlow(false)
    val isParallel: StateFlow<Boolean> = _isParallel.asStateFlow()

    private val _isDeferred = MutableStateFlow(false)
    val isDeferred: StateFlow<Boolean> = _isDeferred.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    private val _resetSettings = MutableSharedFlow<Unit>()
    val resetSettings: SharedFlow<Unit> = _resetSettings.asSharedFlow()

    fun updateCoroutinesCount(count: Float) {
        _coroutinesCount.value = count
    }

    fun updateDispatcher(dispatcher: DispatcherType) {
        _selectedDispatcher.value = dispatcher
    }

    fun updateSequential(enabled: Boolean) {
        _isSequential.value = enabled
        if (enabled) {
            _isParallel.value = false
        } else if (!_isParallel.value) {
            _isParallel.value = true
        }
    }

    fun updateParallel(enabled: Boolean) {
        _isParallel.value = enabled
        if (enabled) {
            _isSequential.value = false
        } else if (!_isSequential.value) {
            _isSequential.value = true
        }
    }

    fun updateDeferred(enabled: Boolean) {
        _isDeferred.value = enabled
    }

    fun startCoroutines() {
        if (_isLoading.value) return

        _isLoading.value = true

        viewModelScope.launch {
            val dispatcher = selectedDispatcher.value.toDispatcher()

            supervisorScope {
                if (isSequential.value) {
                    repeat(coroutinesCount.value.toInt()) { index ->
                        try {
                            if (isDeferred.value) {
                                async(dispatcher, start = CoroutineStart.LAZY) {
                                    performHeavyOperation(index)
                                }.await()
                            } else {
                                withContext(dispatcher) {
                                    performHeavyOperation(index)
                                }
                            }
                        } catch (e: Exception) {
                            handleException(e)
                        }
                    }
                }

                if (isParallel.value) {
                    val deferredJobs = mutableListOf<Deferred<String>>()

                    repeat(coroutinesCount.value.toInt()) { index ->
                        val job = async(dispatcher) {
                            try {
                                performHeavyOperation(index)
                            } catch (e: Exception) {
                                handleException(e)
                                ""
                            }
                        }
                        deferredJobs.add(job)
                    }

                    if (isDeferred.value) {
                        deferredJobs.forEach { it.await() }
                    } else {
                        deferredJobs.awaitAll()
                    }
                }
            }

            _isLoading.value = false
        }
    }

    private suspend fun performHeavyOperation(index: Int): String {
        val delayTime = Random.nextLong(1000, 10001)
        delay(delayTime)

        if (delayTime >= 7000 && Random.nextFloat() < 0.3f) {
            throw when (Random.nextInt(3)) {
                0 -> ToastException()
                1 -> SnackbarException()
                else -> ResetException()
            }
        }

        return "Coroutine $index completed in ${delayTime}ms"
    }

    private suspend fun handleException(e: Exception) {
        when (e) {
            is ToastException -> {
                _toastMessage.emit(e.message ?: "")
            }
            is SnackbarException -> {
                _snackbarMessage.emit(e.message ?: "")
            }
            is ResetException -> {
                resetToDefaults()
                _resetSettings.emit(Unit)
            }
        }
    }

    private fun resetToDefaults() {
        _coroutinesCount.value = 50f
        _selectedDispatcher.value = DispatcherType.DEFAULT
        _isSequential.value = true
        _isParallel.value = false
        _isDeferred.value = false
    }
}

enum class DispatcherType {
    DEFAULT, IO, MAIN;

    fun toDisplayName(): String {
        return when (this) {
            DEFAULT -> "Default"
            IO -> "IO"
            MAIN -> "Main"
        }
    }

    fun toDispatcher(): CoroutineDispatcher {
        return when (this) {
            DEFAULT -> Dispatchers.Default
            IO -> Dispatchers.IO
            MAIN -> Dispatchers.Main
        }
    }
}

class ToastException : Exception("Long operation failed with Toast Exception")
class SnackbarException : Exception("Long operation failed with Snackbar Exception")
class ResetException : Exception("Settings reset due to exception")