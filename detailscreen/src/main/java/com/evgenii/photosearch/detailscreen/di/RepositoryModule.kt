package com.evgenii.photosearch.detailscreen.di

import com.evgenii.photosearch.detailscreen.data.api.PhotoDetailApi
import com.evgenii.photosearch.detailscreen.data.api.PhotoDetailApiBuilder
import com.evgenii.photosearch.detailscreen.data.mapper.PhotoDetailApiMapper
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
    fun providePhotoApi(apiBuilder: PhotoDetailApiBuilder): PhotoDetailApi =
        apiBuilder.buildApi()

    @Provides
    @Singleton
    fun providePhotoSearchByIdRepository(
        api: PhotoDetailApi,
        mapper: PhotoDetailApiMapper
    ): PhotoSearchDetailRepository =
        PhotoSearchDetailRepositoryImpl(api, mapper)
}