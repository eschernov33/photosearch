package com.evgenii.photoofday.presentation.presenters

import androidx.lifecycle.LifecycleOwner
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.evgenii.photoofday.data.service.PhotosService
import com.evgenii.photoofday.domain.model.PhotoItem
import com.evgenii.photoofday.domain.usecases.GetPhotosUseCase
import com.evgenii.photoofday.presentation.contracts.PhotosListContract
import com.evgenii.photoofday.presentation.datasource.DataSourceFactory
import retrofit2.Retrofit

class PhotosListPresenter(
    private val view: PhotosListContract.View,
    retrofit: Retrofit
) : PhotosListContract.Presenter {

    private val getPhotosUseCase = GetPhotosUseCase(retrofit)

    override fun onSearchApply(textSearch: String, lifecycleOwner: LifecycleOwner) {
        if (textSearch.isEmpty()) {
            view.clearPhotosList()
//            view.setListVisible(false)
            view.setErrorMessage("Введите запрос")
        } else {
            val dataSourceFactory = DataSourceFactory(getPhotosUseCase, textSearch)
            val pageListConfig = PagedList.Config.Builder()
                .setPageSize(20)
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