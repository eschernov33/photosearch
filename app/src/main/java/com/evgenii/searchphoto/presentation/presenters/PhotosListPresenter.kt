package com.evgenii.searchphoto.presentation.presenters

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
import com.evgenii.searchphoto.presentation.contracts.PhotosListContract
import com.evgenii.searchphoto.presentation.datasource.DataSourceFactory

class PhotosListPresenter(
    private val view: PhotosListContract.View,
    private val photoSearchRepository: PhotoSearchRepository
) : PhotosListContract.Presenter {

    private var isVisibleList = false

    override fun init(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            isVisibleList = savedInstanceState.getBoolean(KEY_LIST_VISIBLE)
            view.setListVisible(isVisibleList)
        }
    }

    override fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner) {
        if (textSearch.isEmpty()) {
            view.clearPhotosList()
            view.setErrorMessage(R.string.input_query)
        } else {
            isVisibleList = true
            view.setListVisible(isVisibleList)
            searchPhotos(textSearch, lifecycleOwner)
        }
    }

    override fun onItemClick(photoItem: PhotoItem) =
        view.showToast(photoItem.user, photoItem.id)

    override fun onRestartLayout(outState: Bundle) {
        outState.putBoolean(KEY_LIST_VISIBLE, isVisibleList)
    }

    private fun searchPhotos(
        textSearch: String,
        lifecycleOwner: LifecycleOwner,
    ) {
        view.showProgressBar()
        val dataSourceFactory = DataSourceFactory(photoSearchRepository, textSearch, {
            view.clearPhotosList()
            view.setErrorMessage(R.string.error_empty_result)
            view.hideProgressLoading()
        }, {
            view.hideProgressLoading()
        })
        val pageListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
        val liveDataPhotos =
            LivePagedListBuilder(dataSourceFactory, pageListConfig).build()
        liveDataPhotos.observe(lifecycleOwner) { pagedList ->
            view.showPhotosList(pagedList)
            view.hideSoftKeyboard()
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 10
        const val KEY_LIST_VISIBLE = "list_visible"
    }
}