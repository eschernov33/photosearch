package com.evgenii.searchphoto.presentation.contracts

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedList
import com.evgenii.searchphoto.domain.model.PhotoItem

interface PhotosListContract {

    interface View {
        fun setListVisible(visible: Boolean)
        fun showPhotoList(pagedList: PagedList<PhotoItem>)
        fun clearPhotosList()
        fun showToast(user: String, photoId: Int)
        fun setErrorMessage(@StringRes msg: Int)
        fun hideSoftKeyboard()
        fun hideProgressBar()
        fun showProgressBar()
    }

    interface Presenter {
        fun init(savedInstanceState: Bundle?)
        fun onItemClick(photoItem: PhotoItem)
        fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner)
        fun onRestartLayout(outState: Bundle)
    }
}