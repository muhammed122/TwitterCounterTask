package com.example.ui.base
 
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.launch

abstract class BasicFragment<VB : ViewBinding,State : ViewState, Event : ViewEvent, Action : ViewAction>(
    override val bindingInflater: (LayoutInflater) -> VB
) : BaseFragment<VB>(bindingInflater) {

    abstract val viewModel: BaseViewModel<State, Event, Action>

    abstract fun onViewState(state: State)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    onViewState(it)
                }
            }
        }

    }

    protected fun initViewEvent(onViewEvent: (event: Event) -> Unit) {
        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiEvent.collect {
                    onViewEvent(it)
                }
            }
        }
    }
}