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

abstract class BaseFragment<
        ScreenState : BaseScreenState,
        Commands : BaseCommands,
        ViewModel : BaseViewModel<ScreenState, Commands>>(
    viewModelClass: Class<ViewModel>
) : Fragment() {

    protected val navController: NavController by lazy { findNavController() }

    protected val viewModel: ViewModel by lazy { ViewModelProvider(this)[viewModelClass] }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        viewModel.screenState.observe(viewLifecycleOwner, this::renderView)
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

    abstract fun renderView(screenState: ScreenState)

    abstract fun executeCommand(command: Commands)
}