package com.evgenii.searchphoto.presentation.contracts

import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.evgenii.searchphoto.domain.model.PhotoItem

interface PhotosListContract {

    interface View {
        fun showPhotosList(pagedList: PagedList<PhotoItem>)
        fun showToast(message: String)
        fun setListVisible(visible: Boolean)
        fun clearPhotosList()
        fun setErrorMessage(msg: String)
    }

    interface Presenter {
        fun onItemClick(photoItem: PhotoItem)
        fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner)
    }
}