package com.evgenii.photosearch.di

import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.api.RemoteDataSource
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
    fun providePhotoApi(remoteDataSource: RemoteDataSource): PhotosApi =
        remoteDataSource.buildApi()
}