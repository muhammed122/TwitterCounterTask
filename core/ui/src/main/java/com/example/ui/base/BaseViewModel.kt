package com.example.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : ViewState, Event : ViewEvent, Action : ViewAction>(
    val initialState: State,
) : ViewModel() {

    val currentState: State
        get() = uiState.value

    private val _uiState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    val uiState = _uiState.asStateFlow()

    private val _uiEvent: Channel<Event> = Channel()
    val uiEvent = _uiEvent.receiveAsFlow()

    abstract fun handleAction(action: Action)

    protected fun setState(reduce: State.() -> State) {
        _uiState.value = currentState.reduce()
    }
    protected fun setEvent(builder: () -> Event) {
        val effectValue = builder()
        viewModelScope.launch { _uiEvent.send(effectValue) }
    }
}