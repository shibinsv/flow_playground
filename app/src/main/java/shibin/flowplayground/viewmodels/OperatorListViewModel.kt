package shibin.flowplayground.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shibin.flowplayground.di.repository.FlowOperatorRepository
import shibin.flowplayground.model.OperatorListState
import javax.inject.Inject

@HiltViewModel
class OperatorListViewModel @Inject constructor(
    private val repository: FlowOperatorRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OperatorListState())
    val state: StateFlow<OperatorListState> = _state.asStateFlow()

    init {
        loadOperators()
    }

    private fun loadOperators() {
        _state.update { it.copy(isLoading = true) }
        val operators = repository.getOperators()
        _state.update { it.copy(operators = operators, isLoading = false) }
    }
}