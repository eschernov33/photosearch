package com.evgenii.searchphoto.presentation.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.presentation.contracts.PhotosListContract
import com.evgenii.searchphoto.presentation.datasource.DataSourceFactory
import retrofit2.Retrofit

class PhotosListPresenter(
    private val view: PhotosListContract.View,
    private val retrofit: Retrofit
) : PhotosListContract.Presenter {

    override fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner) {
        if (textSearch.isEmpty()) {
            view.clearPhotosList()
//            view.setListVisible(false)
            view.setErrorMessage("Введите запрос")
        } else {
            val dataSourceFactory = DataSourceFactory(retrofit, textSearch)
            val pageListConfig = PagedList.Config.Builder()
                .setPageSize(20)
                .setEnablePlaceholders(true)
                .setPrefetchDistance(10)
                .build()
            val liveDataPhotos =
                LivePagedListBuilder(dataSourceFactory, pageListConfig).build()

            liveDataPhotos.observe(lifecycleOwner, { pagedList ->
                view.showPhotosList(pagedList)
                if (pagedList.isNotEmpty())
                    view.setListVisible(true)
                else
                    view.setErrorMessage("Ничего не найдено")
            })
        }
    }

    override fun onItemClick(photoItem: PhotoItem) {
        view.showToast("User: ${photoItem.user}. ID: ${photoItem.id}")
    }
}