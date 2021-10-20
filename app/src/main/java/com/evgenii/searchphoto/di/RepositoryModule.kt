package com.evgenii.searchphoto.di

import androidx.paging.PagingConfig
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
    fun providePhotoApi(remoteDataSource: RemoteDataSource): PhotosApi =
        remoteDataSource.buildApi()

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
    fun provideRepositoryApi(
        api: PhotosApi,
        mapper: HitApiMapper,
        pagingConfig: PagingConfig
    ): PhotoSearchRepository =
        PhotoSearchRepositoryImpl(api, mapper, pagingConfig)
}