package com.evgenii.searchphoto.domain.usecases

import androidx.paging.PageKeyedDataSource
import com.evgenii.searchphoto.domain.model.LoadResult
import com.evgenii.searchphoto.domain.model.Photo
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository

class LoadAfterPhotoListUseCase(
    private val photoSearchRepository: PhotoSearchRepository,
) {

    fun execute(
        callback: PageKeyedDataSource.LoadCallback<Int, Photo>,
        query: String,
        page: Int,
        onLoadResult: (result: LoadResult) -> Unit
    ) {
        val onResponse: (List<Photo>) -> Unit = { photoList ->
            if (photoList.isEmpty()) {
                callback.onResult(emptyList(), null)
            } else {
                callback.onResult(photoList, page + 1)
            }

        }
        val onFailure: (t: Throwable) -> Unit = {
            onLoadResult(LoadResult.ERROR)
            callback.onResult(emptyList(), null)
        }
        photoSearchRepository.getPhotos(query, page, onResponse, onFailure)
    }
}