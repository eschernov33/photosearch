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
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.evgenii.photosearch.core.presentation.fragments.BaseFragment
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.databinding.PhotosListFragmentBinding
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosAdapter
import com.evgenii.photosearch.photolistscreen.presentation.adapters.PhotosLoadStateAdapter
import com.evgenii.photosearch.photolistscreen.presentation.model.Commands
import com.evgenii.photosearch.photolistscreen.presentation.model.HideKeyboard
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.ShowDetail
import com.evgenii.photosearch.photolistscreen.presentation.viewmodel.PhotoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        initSearchField()
        setRetryButtonListener()
    }

    private fun setAnimationSharedTransition() {
        postponeEnterTransition()
        binding.photoListBlock.rvPhotoList.viewTreeObserver.addOnPreDrawListener(preDrawListener)
    }

    private fun initObservers() {
        initPhotoListStateScreenObserver()
        initPhotoListObserver()
        initCommandsObserver()
    }

    private fun initPhotoListAdapter() {
        binding.photoListBlock.rvPhotoList.adapter =
            adapter.withLoadStateFooter(PhotosLoadStateAdapter())
        adapter.addLoadStateListener(loadStateListener)
    }

    private fun initSearchField() {
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            text?.let { query ->
                viewModel.onSearchTextChanged(query.toString())
            }
        }
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.onSearchButtonClick()
                true
            } else {
                false
            }
        }
    }

    private fun setRetryButtonListener() =
        binding.photoListBlock.btnRetrySearch.setOnClickListener {
            viewModel.onRetryButtonClick()
        }

    private fun initPhotoListStateScreenObserver() =
        viewModel.screenState.observe(viewLifecycleOwner) { photoListStateScreen ->
            with(photoListStateScreen) {
                with(binding.photoListBlock) {
                    root.isVisible = isContentBlockVisible
                    rvPhotoList.isVisible = isPhotoListVisible
                    progressBarLoadPhotos.isVisible = isLoadingProgressBarVisible
                    tvSearchErrorMessage.isVisible = isErrorTextVisible
                    btnRetrySearch.isVisible = isRetryButtonVisible
                    tvSearchErrorMessage.text = getString(errorTextResId)
                }
            }
        }

    private fun initPhotoListObserver() =
        viewModel.photoListUpdated.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, PagingData.empty())
            lifecycleScope.launch {
                viewModel.photoListFlow?.collectLatest {
                    adapter.submitData(it)
                }
            }
        }

    private fun initCommandsObserver() =
        viewModel.commands.observe(viewLifecycleOwner) { commandEvent ->
            when (val command: Commands? = commandEvent.getValue()) {
                is HideKeyboard -> hideSoftKeyboard()
                is ShowDetail -> navigateToDetailScreen(command.photoItem)
            }
        }

    private fun onItemClick(
        extras: FragmentNavigator.Extras,
        photoItem: PhotoItem
    ) {
        transitionExtras = extras
        viewModel.onPhotoItemClick(photoItem)
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