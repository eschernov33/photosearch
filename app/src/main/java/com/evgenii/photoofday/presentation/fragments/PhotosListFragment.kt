package com.evgenii.photoofday.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import com.evgenii.photoofday.App
import com.evgenii.photoofday.databinding.PhotosListFragmentBinding
import com.evgenii.photoofday.domain.model.PhotoItem
import com.evgenii.photoofday.presentation.adapters.PhotosAdapter
import com.evgenii.photoofday.presentation.contracts.PhotosListContract
import com.evgenii.photoofday.presentation.presenters.PhotosListPresenter

class PhotosListFragment : Fragment(), PhotosListContract.View {

    private val adapter = PhotosAdapter { photoItem ->
        presenter.onItemClick(photoItem)
    }

    private lateinit var presenter: PhotosListContract.Presenter

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
        initPhotoList()
        initPresenter()
        setEditTextListener()
    }

    private fun initPresenter() {
        val app = requireContext().applicationContext as App
        presenter = PhotosListPresenter(this, app.retrofit)
    }

    private fun setEditTextListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                presenter.onSearchApply(
                    binding.etSearch.text.toString(),
                    this@PhotosListFragment
                )
                true
            } else
                false
        }
    }

    override fun setListVisible(visible: Boolean) {
        binding.rvMarsPhotos.isVisible = visible
    }

    private fun initPhotoList() {
        binding.rvMarsPhotos.adapter = adapter
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    override fun showPhotosList(pagedList: PagedList<PhotoItem>) {
        adapter.submitList(pagedList)
    }

    override fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun clearPhotosList() {
        adapter.submitList(null)
    }

    override fun setErrorMessage(msg: String) {
        binding.etSearch.error = msg
    }
}