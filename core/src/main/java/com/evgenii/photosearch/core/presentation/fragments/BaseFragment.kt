package com.evgenii.photosearch.core.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.evgenii.photosearch.core.presentation.model.BaseCommands
import com.evgenii.photosearch.core.presentation.model.BaseScreenState
import com.evgenii.photosearch.core.presentation.viewmodel.BaseViewModel

abstract class BaseFragment<SS : BaseScreenState, C : BaseCommands, VM : BaseViewModel<SS, C>>(
    viewModelClass: Class<VM>
) : Fragment() {

    protected val navController: NavController by lazy { findNavController() }

    protected val viewModel: VM by lazy { ViewModelProvider(this)[viewModelClass] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.screenState.observe(viewLifecycleOwner, this::updateScreen)
        viewModel.commands.observe(viewLifecycleOwner) { event ->
            event.getValue()?.let(this::executeCommand)
        }
    }

    protected fun showToast(message: String) =
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()

    protected fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireView().windowToken,
                InputMethodManager.RESULT_UNCHANGED_SHOWN
            )
        }
    }

    abstract fun updateScreen(screenState: SS)

    abstract fun executeCommand(command: C)
}