package com.evgenii.photosearch.detailscreen.di

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.PhotoApiMapper
import com.evgenii.photosearch.detailscreen.data.repository.PhotoSearchDetailRepositoryImpl
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchDetailRepository
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
    fun providePhotoSearchByIdRepository(
        api: PhotosApi,
        mapper: PhotoApiMapper
    ): PhotoSearchDetailRepository =
        PhotoSearchDetailRepositoryImpl(api, mapper)
}