package com.evgenii.searchphoto.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import com.evgenii.searchphoto.App
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.databinding.PhotosListFragmentBinding
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.presentation.adapters.PhotosAdapter
import com.evgenii.searchphoto.presentation.contracts.PhotosListContract
import com.evgenii.searchphoto.presentation.presenters.PhotosListPresenter

class PhotosListFragment : Fragment(), PhotosListContract.View {

    private lateinit var presenter: PhotosListContract.Presenter

    private val adapter = PhotosAdapter { photoItem ->
        presenter.onItemClick(photoItem)
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
        initPhotoListAdapter()
        initPresenter()
        setEditTextListener()
    }

    private fun initPhotoListAdapter() {
        binding.rvMarsPhotos.adapter = adapter
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
                    this
                )
                true
            } else
                false
        }
    }

    override fun setListVisible(visible: Boolean) {
        binding.rvMarsPhotos.isVisible = visible
    }

    override fun showPhotosList(pagedList: PagedList<PhotoItem>) =
        adapter.submitList(pagedList)

    override fun showToast(user: String, photoId: Int) =
        Toast.makeText(requireContext(),
            getString(R.string.toast_message, user, photoId),
            Toast.LENGTH_SHORT).show()

    override fun clearPhotosList() =
        adapter.submitList(null)

    override fun setErrorMessage(msg: Int) {
        binding.etSearch.error = resources.getString(msg)
    }
}