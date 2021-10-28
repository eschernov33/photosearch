package com.evgenii.photosearch.photolistscreen.di

import androidx.paging.PagingConfig
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.photolistscreen.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providePhotoSearchListRepository(
        api: PhotosApi
    ): PhotoSearchRepository =
        PhotoSearchRepositoryImpl(
            api, PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = ENABLE_PLACEHOLDER,
                initialLoadSize = INITIAL_LOAD_SIZE,
                maxSize = MAX_SIZE
            )
        )

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 1
        private const val ENABLE_PLACEHOLDER = true
        private const val INITIAL_LOAD_SIZE = 20
        private const val MAX_SIZE = 200
    }
}