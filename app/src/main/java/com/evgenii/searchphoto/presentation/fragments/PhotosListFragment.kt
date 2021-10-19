package com.evgenii.searchphoto.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.databinding.PhotosListFragmentBinding
import com.evgenii.searchphoto.presentation.adapters.PhotosAdapter
import com.evgenii.searchphoto.presentation.adapters.PhotosAdapterLoadState
import com.evgenii.searchphoto.presentation.model.*
import com.evgenii.searchphoto.presentation.viewmodel.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment : Fragment() {

    private val viewModel: PhotoListViewModel by viewModels()
    private val navController: NavController by lazy { findNavController() }
    private var transitionExtras: FragmentNavigator.Extras? = null

    private var _binding: PhotosListFragmentBinding? = null
    private val binding: PhotosListFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private val preDrawListener = {
        startPostponedEnterTransition()
        true
    }

    private val adapter = PhotosAdapter { photoItem, extras ->
        onItemClick(extras, photoItem)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = PhotosListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimSharedTransition()
        initObservers()
        initPhotoListAdapter()
        setEditTextListener()
    }

    private fun setAnimSharedTransition() {
        postponeEnterTransition()
        binding.areaPhotoList.rvPhotoList.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    private fun initObservers() {
        with(viewModel) {
            photosLoadState.observe(viewLifecycleOwner) { photosLoadState ->
                when (photosLoadState) {
                    is OnInitState -> {
                        binding.areaPhotoList.root.isVisible = false
                        binding.areaPhotoList.progressBarLoadPhotos.isVisible = false
                        binding.areaPhotoList.rvPhotoList.isVisible = false
                        binding.areaPhotoList.tvSearchErrorMessage.isVisible = false
                    }
                    is StartLoadingState -> {
                        binding.areaPhotoList.root.isVisible = true
                        binding.areaPhotoList.progressBarLoadPhotos.isVisible = true
                        hideSoftKeyboard()
                    }
                    is EndLoadingState -> {
                        binding.areaPhotoList.progressBarLoadPhotos.isVisible = false
                    }
                    is ErrorLoadState -> {
                        binding.areaPhotoList.tvSearchErrorMessage.text =
                            getString(R.string.error_load_description, photosLoadState.errorMessage)
                        binding.areaPhotoList.tvSearchErrorMessage.isVisible = true
                        binding.areaPhotoList.rvPhotoList.isVisible = false
                    }
                    is SuccessLoadState -> {
                        binding.areaPhotoList.rvPhotoList.isVisible = true
                        binding.areaPhotoList.tvSearchErrorMessage.isVisible = false
                        adapter.submitData(lifecycle, photosLoadState.pagingData)
                    }
                    is EmptyLoadState -> {
                        binding.areaPhotoList.tvSearchErrorMessage.text =
                            getString(R.string.error_photos_not_found)
                        binding.areaPhotoList.tvSearchErrorMessage.isVisible = true
                        binding.areaPhotoList.rvPhotoList.isVisible = false
                    }
                }
            }
            actionShowDetails.observe(viewLifecycleOwner) {
                it.getValue()?.let(this@PhotosListFragment::navigateToDetailScreen)
            }
        }
    }

    private fun initPhotoListAdapter() {
        binding.areaPhotoList.rvPhotoList.adapter = adapter.withLoadStateFooter(PhotosAdapterLoadState())
        adapter.addLoadStateListener { loadState ->
            viewModel.onLoadStateListener(loadState, adapter.itemCount)
        }
    }

    private fun setEditTextListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPhotos(binding.etSearch.text.toString())
                viewModel.photoList.observe(viewLifecycleOwner) { pagingData ->
                    adapter.submitData(lifecycle, pagingData)
                }
                true
            } else
                false
        }
    }

    private fun onItemClick(
        extras: FragmentNavigator.Extras,
        photoItem: PhotoItem
    ) {
        transitionExtras = extras
        viewModel.onPhotoDetails(photoItem)
    }

    private fun navigateToDetailScreen(photoItem: PhotoItem) {
        transitionExtras?.let { extras ->
            navController.navigate(
                PhotosListFragmentDirections.actionPhotosListFragmentToPhotoDetailFragment(
                    photoItem.id, photoItem.largeImageURL
                ), extras
            )
        }
    }

    private fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireView().windowToken,
                RESULT_UNCHANGED_SHOWN
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.areaPhotoList.rvPhotoList.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        _binding = null
    }
}