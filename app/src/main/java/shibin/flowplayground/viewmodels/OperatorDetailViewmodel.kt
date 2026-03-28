package shibin.flowplayground.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import shibin.flowplayground.di.repository.FlowOperatorRepository
import shibin.flowplayground.enums.DemoType
import shibin.flowplayground.model.OperatorDetailState
import javax.inject.Inject

@HiltViewModel
class OperatorDetailViewModel @Inject constructor(
    private val repository: FlowOperatorRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val operatorId: String = checkNotNull(savedStateHandle["operatorId"])

    private val _state = MutableStateFlow(OperatorDetailState())
    val state: StateFlow<OperatorDetailState> = _state.asStateFlow()

    private var demoJob: Job? = null

    init {
        loadOperator()
    }

    private fun loadOperator() {
        val operator = repository.getOperatorById(operatorId)
        _state.update { it.copy(operator = operator) }
    }

    fun onDemoInputChanged(input: String) {
        _state.update { it.copy(demoInput = input) }
    }

    fun runDemo() {
        demoJob?.cancel()
        _state.update { it.copy(demoOutput = emptyList(), isDemoRunning = true) }

        val operator = _state.value.operator ?: return
        val input = _state.value.demoInput

        demoJob = viewModelScope.launch {
            runDemoForOperator(operator.demoType, input)
            _state.update { it.copy(isDemoRunning = false) }
        }
    }

    fun stopDemo() {
        demoJob?.cancel()
        _state.update { it.copy(isDemoRunning = false) }
    }

    private suspend fun runDemoForOperator(demoType: DemoType, input: String) {
        when (demoType) {
            DemoType.MAP -> mapDemo(input)
            DemoType.FILTER -> filterDemo(input)
            DemoType.FLAT_MAP_LATEST -> flatMapLatestDemo(input)
            DemoType.FLAT_MAP_MERGE -> flatMapMergeDemo()
            DemoType.DEBOUNCE -> debounceDemo(input)
            DemoType.DISTINCT_UNTIL_CHANGED -> distinctUntilChangedDemo()
            DemoType.ZIP -> zipDemo()
            DemoType.COMBINE -> combineDemo()
            DemoType.BUFFER -> bufferDemo()
            DemoType.COLLECT_LATEST -> collectLatestDemo()
        }
    }

    // --- Demo implementations ---

    private suspend fun mapDemo(input: String) {
        val multiplier = input.toIntOrNull() ?: 2
        flowOf(1, 2, 3, 4, 5)
            .map { it * multiplier }
            .collect { appendOutput("map($it) → ${it}") }
    }

    private suspend fun filterDemo(input: String) {
        val threshold = input.toIntOrNull() ?: 3
        flowOf(1, 2, 3, 4, 5)
            .filter { it >= threshold }
            .onEach { delay(300) }
            .collect { appendOutput("filter → kept $it") }
    }

    private suspend fun flatMapLatestDemo(input: String) {
        val queries = input.ifBlank { "a,ab,abc" }.split(",")
        flow {
            queries.forEach {
                emit(it.trim())
                delay(200)
            }
        }
            .flatMapLatest { query ->
                flow {
                    appendOutput("⚡ New query: '$query' — cancelling previous")
                    delay(500) // simulate API call
                    emit("Result for '$query'")
                }
            }
            .collect { appendOutput("✅ Collected: $it") }
    }

    private suspend fun flatMapMergeDemo() {
        flowOf(1, 2, 3)
            .flatMapMerge { id ->
                flow {
                    delay((100..400L).random())
                    emit("📦 Response for request #$id")
                }
            }
            .collect { appendOutput(it) }
    }

    private suspend fun debounceDemo(input: String) {
        val keystrokes = input.ifBlank { "h,he,hel,hell,hello" }.split(",")
        flow {
            keystrokes.forEach {
                emit(it.trim())
                delay(150)
            }
        }
            .debounce(300)
            .collect { appendOutput("🔍 Search triggered for: '$it'") }
    }

    private suspend fun distinctUntilChangedDemo() {
        flowOf(1, 1, 2, 2, 2, 3, 1, 1)
            .onEach { delay(300) }
            .distinctUntilChanged()
            .collect { appendOutput("✅ Emitted: $it") }
    }

    private suspend fun zipDemo() {
        val flowA = flow {
            listOf("🍎", "🍌", "🍇").forEach {
                emit(it); delay(400)
            }
        }
        val flowB = flow {
            listOf("Red", "Yellow", "Purple").forEach {
                emit(it); delay(600)
            }
        }
        flowA.zip(flowB) { fruit, color -> "$fruit is $color" }
            .collect { appendOutput("zip → $it") }
    }

    private suspend fun combineDemo() {
        val flowA = flow {
            listOf("Alice", "Bob", "Carol").forEach {
                emit(it); delay(400)
            }
        }
        val flowB = flow {
            listOf("Online", "Away", "Offline").forEach {
                emit(it); delay(600)
            }
        }
        combine(flowA, flowB) { name, status -> "$name is $status" }
            .collect { appendOutput("combine → $it") }
    }

    private suspend fun bufferDemo() {
        flow {
            repeat(5) {
                appendOutput("⚡ Produced: $it")
                emit(it)
                delay(100) // fast producer
            }
        }
            .buffer(capacity = 5)
            .collect {
                delay(400) // slow consumer
                appendOutput("🐢 Consumed: $it")
            }
    }

    private suspend fun collectLatestDemo() {
        flow {
            repeat(5) {
                emit(it)
                delay(200)
            }
        }
            .collectLatest { value ->
                appendOutput("⚡ Started processing: $value")
                delay(350) // longer than emission interval → gets cancelled
                appendOutput("✅ Finished: $value") // only last one reaches here
            }
    }

    private fun appendOutput(line: String) {
        _state.update { it.copy(demoOutput = it.demoOutput + line) }
    }
}