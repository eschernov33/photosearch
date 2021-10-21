package com.evgenii.photosearch.detailscreen.di

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.detailscreen.data.repository.PhotoSearchByIdRepositoryImpl
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchByIdRepository
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
        mapper: HitApiMapper
    ): PhotoSearchByIdRepository =
        PhotoSearchByIdRepositoryImpl(api, mapper)
}