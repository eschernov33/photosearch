package com.evgenii.photosearch.photolistscreen.presentation.fragments

import android.content.Context
import android.net.Uri
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
import androidx.paging.CombinedLoadStates
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.databinding.PhotosListFragmentBinding
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosAdapter
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosAdapterLoadState
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorMessage
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.viewmodel.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private val viewModel: PhotoListViewModel by viewModels()
    private val navController: NavController by lazy { findNavController() }
    private var transitionExtras: FragmentNavigator.Extras? = null

    private var _binding: PhotosListFragmentBinding? = null
    private val binding: PhotosListFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private var _adapter: PhotosAdapter? = PhotosAdapter { photoItem, extras ->
        onItemClick(extras, photoItem)
    }
    private val adapter: PhotosAdapter
        get() = _adapter ?: throw RuntimeException("Photos adapter is null")

    private val preDrawListener = {
        startPostponedEnterTransition()
        true
    }

    private val loadStateListener: (CombinedLoadStates) -> Unit = { loadState ->
        viewModel.onLoadStateListener(loadState, adapter.itemCount)
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

    private fun initObservers() =
        with(viewModel) {
            photoListAreaViewsVisibility.observe(viewLifecycleOwner) { photoListAreaParam ->
                binding.areaPhotoList.root.isVisible = photoListAreaParam.areaVisible
                binding.areaPhotoList.rvPhotoList.isVisible = photoListAreaParam.listVisible
                binding.areaPhotoList.progressBarLoadPhotos.isVisible =
                    photoListAreaParam.progressPhotoLoadVisible
                binding.areaPhotoList.tvSearchErrorMessage.isVisible =
                    photoListAreaParam.errorMessageVisible
            }
            photoList.observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
            }
            errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                when (errorMessage.type) {
                    ErrorMessage.Type.NOT_FOUND -> {
                        binding.areaPhotoList.tvSearchErrorMessage.text =
                            getString(R.string.error_empty_result)
                    }
                    ErrorMessage.Type.NETWORK -> {
                        binding.areaPhotoList.tvSearchErrorMessage.text =
                            getString(R.string.error_load_description, errorMessage.message)
                    }
                }

            }
            actionShowDetails.observe(viewLifecycleOwner) { event ->
                event.getValue()?.let(this@PhotoListFragment::navigateToDetailScreen)
            }
            actionHideKeyboard.observe(viewLifecycleOwner) { event ->
                event.getValue()?.let { hideSoftKeyboard() }
            }
        }

    private fun initPhotoListAdapter() {
        binding.areaPhotoList.rvPhotoList.adapter =
            adapter.withLoadStateFooter(PhotosAdapterLoadState())
        adapter.addLoadStateListener(loadStateListener)
    }

    private fun setEditTextListener() =
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

    private fun onItemClick(
        extras: FragmentNavigator.Extras,
        photoItem: PhotoItem
    ) {
        transitionExtras = extras
        viewModel.onPhotoDetails(photoItem)
    }

    private fun navigateToDetailScreen(photoItem: PhotoItem) {
        transitionExtras?.let { extras ->
            val uri =
                Uri.parse("app://photoDetailFragment/?photoId=${photoItem.id}&largeImageUrl=${photoItem.largeImageURL}")
            navController.navigate(
                uri, null, extras
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
        adapter.removeLoadStateListener { loadStateListener }
        _adapter = null
        _binding = null
    }
}