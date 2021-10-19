package com.evgenii.searchphoto.di

import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.api.RemoteDataSource
import com.evgenii.searchphoto.data.mapper.HitApiMapper
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
import com.evgenii.searchphoto.domain.repository.PhotoSearchRepository
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
    fun providePhotoApi(remoteDataSource: RemoteDataSource): PhotosApi{
        return remoteDataSource.buildApi()
    }

    @Provides
    @Singleton
    fun provideRepositoryApi(api: PhotosApi, mapper: HitApiMapper): PhotoSearchRepository{
        return PhotoSearchRepositoryImpl(api, mapper)
    }
}