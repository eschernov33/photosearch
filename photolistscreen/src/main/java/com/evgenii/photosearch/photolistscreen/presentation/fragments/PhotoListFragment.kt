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
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import com.evgenii.photosearch.core.presentation.fragments.BaseFragment
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.databinding.PhotosListFragmentBinding
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosAdapter
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosAdapterLoadState
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorType
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.viewmodel.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PhotoListFragment : BaseFragment() {

    private val viewModel: PhotoListViewModel by viewModels()
    private val navController: NavController by lazy { findNavController() }
    private var transitionExtras: FragmentNavigator.Extras? = null

    private var _binding: PhotosListFragmentBinding? = null
    private val binding: PhotosListFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

    private var _adapter: PhotosAdapter? = null
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
        _adapter = PhotosAdapter { photoItem, extras ->
            onItemClick(extras, photoItem)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimationSharedTransition()
        initObservers()
        initPhotoListAdapter()
        setEditTextListener()
        setRetryButtonListener()
    }

    private fun setAnimationSharedTransition() {
        postponeEnterTransition()
        binding.photoListBlock.rvPhotoList.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    private fun initObservers() {
        initPhotoListViewsVisibilityObserver()
        initPhotoListObserver()
        initErrorMessageObserver()
        initEventsObserver()
    }

    private fun initPhotoListAdapter() {
        binding.photoListBlock.rvPhotoList.adapter =
            adapter.withLoadStateFooter(PhotosAdapterLoadState())
        adapter.addLoadStateListener(loadStateListener)
    }

    private fun setEditTextListener() =
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchPhotos(binding.etSearch.text.toString())
                true
            } else
                false
        }

    private fun setRetryButtonListener() =
        binding.photoListBlock.btnRetrySearch.setOnClickListener {
            viewModel.onRetryClick(binding.etSearch.text.toString())
        }

    private fun initPhotoListViewsVisibilityObserver() =
        viewModel.photoListViewsVisibility.observe(viewLifecycleOwner) { photoListBlockParam ->
            with(binding.photoListBlock) {
                root.isVisible = photoListBlockParam.areaVisible
                rvPhotoList.isVisible = photoListBlockParam.listVisible
                progressBarLoadPhotos.isVisible = photoListBlockParam.progressPhotoLoadVisible
                tvSearchErrorMessage.isVisible = photoListBlockParam.errorMessageVisible
                btnRetrySearch.isVisible = photoListBlockParam.btnRetryVisible
            }
        }

    private fun initPhotoListObserver() =
        viewModel.photoList.observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

    private fun initErrorMessageObserver() =
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorType ->
            when (errorType) {
                ErrorType.NOT_FOUND -> {
                    binding.photoListBlock.tvSearchErrorMessage.text =
                        getString(R.string.error_empty_result)
                }
                else -> {
                    binding.photoListBlock.tvSearchErrorMessage.text =
                        getString(R.string.error_load_description)
                }
            }
        }

    private fun initEventsObserver() {
        viewModel.eventShowDetails.observe(viewLifecycleOwner) { event ->
            event.getValue()?.let(::navigateToDetailScreen)
        }
        viewModel.eventHideKeyboard.observe(viewLifecycleOwner) { event ->
            event.getValue()?.let { hideSoftKeyboard() }
        }
    }

    private fun onItemClick(
        extras: FragmentNavigator.Extras,
        photoItem: PhotoItem
    ) {
        transitionExtras = extras
        viewModel.onPhotoDetails(photoItem)
    }

    private fun navigateToDetailScreen(photoItem: PhotoItem) =
        transitionExtras?.let { extras ->
            val uri = Uri.parse(
                getString(
                    R.string.deeplink_detail_screen,
                    photoItem.id,
                    photoItem.largeImageURL
                )
            )
            navController.navigate(
                uri, null, extras
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding.photoListBlock.rvPhotoList.viewTreeObserver.removeOnPreDrawListener(preDrawListener)
        adapter.removeLoadStateListener { loadStateListener }
        _adapter = null
        _binding = null
    }
}