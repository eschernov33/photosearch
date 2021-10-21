package com.evgenii.photosearch.photolistscreen.di

import androidx.paging.PagingConfig
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
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
    fun providePagingConfig(): PagingConfig =
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = true,
            prefetchDistance = 5
        )

    @Provides
    @Singleton
    fun providePhotoSearchListRepository(
        api: PhotosApi,
        mapper: HitApiMapper,
        pagingConfig: PagingConfig
    ): PhotoSearchRepository =
        PhotoSearchRepositoryImpl(api, mapper, pagingConfig)
}