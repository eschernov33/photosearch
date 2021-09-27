package com.evgenii.searchphoto.presentation.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.presentation.contracts.PhotosListContract
import com.evgenii.searchphoto.presentation.datasource.DataSourceFactory
import retrofit2.Retrofit

class PhotosListPresenter(
    private val view: PhotosListContract.View,
    private val retrofit: Retrofit,
) : PhotosListContract.Presenter {

    override fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner) {
        if (textSearch.isEmpty()) {
            view.clearPhotosList()
            view.setErrorMessage(R.string.input_query)
        } else {
            view.setListVisible(true)
            searchPhotos(textSearch, lifecycleOwner)
        }
    }

    private fun searchPhotos(
        textSearch: String,
        lifecycleOwner: LifecycleOwner,
    ) {
        val dataSourceFactory = DataSourceFactory(retrofit, textSearch)
        val pageListConfig = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setPrefetchDistance(PREFETCH_DISTANCE)
            .build()
        val liveDataPhotos =
            LivePagedListBuilder(dataSourceFactory, pageListConfig).build()

        liveDataPhotos.observe(lifecycleOwner) { pagedList ->
            view.showPhotosList(pagedList)
        }
    }

    override fun onItemClick(photoItem: PhotoItem) {
        view.showToast(photoItem.user, photoItem.id)
    }

    companion object {
        const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 10
    }
}