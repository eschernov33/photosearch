package com.evgenii.searchphoto.domain.usecases

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class LoadInitialPhotoListUseCase(
    private val photoSearchRepository: PhotoSearchRepository,
) {

    fun execute(
        callback: PageKeyedDataSource.LoadInitialCallback<Int, PhotoItem>,
        query: String,
        page: Int,
        onLoadResult: (result: LoadResult) -> Unit
    ) {
        val onResponse: (List<PhotoItem>) -> Unit = { photoList ->
            if (photoList.isEmpty()) {
                callback.onResult(emptyList(), null, null)
                onLoadResult(LoadResult.EMPTY)
            } else {
                callback.onResult(photoList, null, page)
                onLoadResult(LoadResult.SUCCESS)
            }
        }
        val onFailure: (t: Throwable) -> Unit = {
            onLoadResult(LoadResult.ERROR)
            callback.onResult(emptyList(), null, null)
        }
        photoSearchRepository.getPhotos(query, page, onResponse, onFailure)
    }

}