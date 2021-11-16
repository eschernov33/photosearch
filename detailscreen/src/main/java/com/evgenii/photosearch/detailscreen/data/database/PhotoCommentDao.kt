package com.evgenii.photosearch.detailscreen.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment

@Dao
interface PhotoCommentDao {

    @Query("SELECT * FROM photocomment WHERE photoId = :id")
    suspend fun getPhotoCommentById(id: Int): PhotoComment?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotoComment(photoComment: PhotoComment)
}