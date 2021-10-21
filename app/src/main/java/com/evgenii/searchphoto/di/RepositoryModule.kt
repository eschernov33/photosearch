package com.evgenii.searchphoto.di

import androidx.paging.PagingConfig
import com.evgenii.core.data.mapper.HitApiMapper
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoSearchByIdRepository
import com.evgenii.photosearch.photolistscreen.domain.repository.PhotoSearchRepository
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.data.api.RemoteDataSource
import com.evgenii.searchphoto.data.repository.PhotoSearchByIdRepositoryImpl
import com.evgenii.searchphoto.data.repository.PhotoSearchRepositoryImpl
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

    @Provides
    @Singleton
    fun provideRepositoryByIdApi(
        api: PhotosApi,
        mapper: HitApiMapper,
        pagingConfig: PagingConfig
    ): PhotoSearchByIdRepository =
        PhotoSearchByIdRepositoryImpl(api, mapper, pagingConfig)
}