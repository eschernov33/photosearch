package com.evgenii.photosearch.core.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evgenii.photosearch.core.presentation.model.BaseCommands
import com.evgenii.photosearch.core.presentation.model.BaseScreenState
import com.evgenii.photosearch.core.presentation.model.Event

abstract class BaseViewModel<ScreenState : BaseScreenState, Commands : BaseCommands> : ViewModel() {

    private val _commands: MutableLiveData<Event<Commands>> = MutableLiveData()
    val commands: LiveData<Event<Commands>> = _commands

    private val _screenState: MutableLiveData<ScreenState> = MutableLiveData()
    val screenState: LiveData<ScreenState> = _screenState

    protected fun executeCommand(command: Commands) {
        _commands.value = Event(command)
    }

    protected fun updateScreen(screenState: ScreenState) {
        _screenState.value = screenState
    }
}