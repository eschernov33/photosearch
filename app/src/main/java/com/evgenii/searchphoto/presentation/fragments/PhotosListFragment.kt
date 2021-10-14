package com.evgenii.searchphoto.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.evgenii.searchphoto.App
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.searchphoto.databinding.PhotosListFragmentBinding
import com.evgenii.searchphoto.domain.usecases.LoadPhotoListUseCase
import com.evgenii.searchphoto.presentation.adapters.PhotosAdapter
import com.evgenii.searchphoto.presentation.adapters.PhotosAdapterLoadState
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper
import com.evgenii.searchphoto.presentation.viewmodel.PhotoListViewModel
import com.evgenii.searchphoto.presentation.viewmodel.PhotoListViewModelFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotosListFragment : Fragment() {

    private val viewModel: PhotoListViewModel by viewModels() {
        val app = requireContext().applicationContext as App
        val repository = PhotoSearchRepositoryImpl()
        val loadPhotoListUseCase = LoadPhotoListUseCase(repository)
        val mapper = PhotoItemMapper()
        PhotoListViewModelFactory(app.photosApi, loadPhotoListUseCase, mapper)
    }

    private val adapter = PhotosAdapter { photoItem ->
        viewModel.onItemClick(photoItem)
    }

    private var _binding: PhotosListFragmentBinding? = null
    private val binding: PhotosListFragmentBinding
        get() = _binding ?: throw RuntimeException("PhotosListFragmentBinding is null")

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
        initObservers()
        initPhotoListAdapter()
        setEditTextListener()
    }

    private fun initObservers() {
        with(viewModel) {
            showProgressBar.observe(viewLifecycleOwner) { isVisible ->
                binding.progressBarLoadPhotos.isVisible = isVisible
            }
            photoListVisibility.observe(viewLifecycleOwner) { isVisible ->
                binding.rvPhotoList.isVisible = isVisible
            }

            textSearchResultError.observe(viewLifecycleOwner) { text ->
                binding.tvSearchInfo.text = text
            }
            textSearchResultErrorVisible.observe(viewLifecycleOwner) { isVisible ->
                binding.tvSearchInfo.isVisible = isVisible
            }
            shouldHideSoftKeyboard.observe(viewLifecycleOwner) {
                hideSoftKeyboard()
            }
            shouldShowToast.observe(viewLifecycleOwner, this@PhotosListFragment::showToast)
        }
    }

    private fun initPhotoListAdapter() {
        binding.rvPhotoList.adapter = adapter.withLoadStateFooter(PhotosAdapterLoadState())
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

    private fun showToast(userName: String) =
        Toast.makeText(
            requireContext(),
            getString(R.string.toast_message, userName),
            Toast.LENGTH_SHORT
        ).show()

    private fun hideSoftKeyboard() {
        val inputMethodManager: InputMethodManager = requireContext().getSystemService(
            Context.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        if (inputMethodManager.isAcceptingText) {
            inputMethodManager.hideSoftInputFromWindow(
                requireView().windowToken,
                0
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}