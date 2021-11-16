package com.evgenii.photosearch.detailscreen.di

import android.app.Application
import androidx.room.Room
import com.evgenii.photosearch.detailscreen.data.database.PhotoCommentDataBase
import com.evgenii.photosearch.detailscreen.data.database.PhotoCommentDataBase.Companion.DB_NAME
import com.evgenii.photosearch.detailscreen.data.repository.PhotoCommentRepositoryImpl
import com.evgenii.photosearch.detailscreen.domain.repository.PhotoCommentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryDBModule {

    @Provides
    @Singleton
    fun providePhotoCommentDatabase(app: Application): PhotoCommentDataBase =
        Room.databaseBuilder(
            app, PhotoCommentDataBase::class.java, DB_NAME
        ).build()

    @Provides
    @Singleton
    fun providePhotoCommentRepository(db: PhotoCommentDataBase): PhotoCommentRepository =
        PhotoCommentRepositoryImpl(db.photoCommentDao)
}