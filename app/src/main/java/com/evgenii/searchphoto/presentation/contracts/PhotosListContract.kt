package com.evgenii.searchphoto.presentation.contracts

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.paging.PagedList
import com.evgenii.searchphoto.presentation.model.PhotoItem

interface PhotosListContract {

    interface View {
        fun setListVisibility(isVisible: Boolean)
        fun showPhotoList(pagedList: PagedList<PhotoItem>)
        fun clearPhotosList()
        fun showToast(userName: String, photoId: Int)
        fun setErrorMessage(@StringRes message: Int)
        fun hideSoftKeyboard()
        fun hideProgressBar()
        fun showProgressBar()
    }

    interface Presenter {
        fun init(savedInstanceState: Bundle?)
        fun onItemClick(photo: PhotoItem)
        fun onSearchApply(textSearch: String)
        fun onRestartLayout(outState: Bundle)
        fun onDestroyFragment()
    }
}