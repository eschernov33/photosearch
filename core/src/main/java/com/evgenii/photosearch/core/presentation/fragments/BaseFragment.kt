package com.evgenii.photosearch.core.presentation.fragments

import android.widget.Toast
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun showToast(message: String) =
        Toast.makeText(
            requireContext(),
            message,
            Toast.LENGTH_LONG
        ).show()
}