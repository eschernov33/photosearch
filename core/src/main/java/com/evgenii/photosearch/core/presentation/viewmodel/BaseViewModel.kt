package com.evgenii.photosearch.core.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evgenii.photosearch.core.presentation.model.BaseCommands
import com.evgenii.photosearch.core.presentation.model.BaseScreenState
import com.evgenii.photosearch.core.presentation.model.Event

abstract class BaseViewModel<BSS : BaseScreenState, BC : BaseCommands> : ViewModel() {

    private val _commands: MutableLiveData<Event<BC>> = MutableLiveData()
    val commands: LiveData<Event<BC>> = _commands

    private val _screenState: MutableLiveData<BSS> = MutableLiveData()
    val screenState: LiveData<BSS> = _screenState

    protected fun executeCommand(command: BC) {
        _commands.value = Event(command)
    }

    protected fun updateScreen(screenState: BSS) {
        _screenState.value = screenState
    }
}