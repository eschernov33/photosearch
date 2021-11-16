package com.evgenii.photosearch.detailscreen.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.evgenii.photosearch.detailscreen.domain.model.PhotoComment

@Database(
    entities = [PhotoComment::class],
    version = 1
)
abstract class PhotoCommentDataBase : RoomDatabase() {

    abstract val photoCommentDao: PhotoCommentDao

    companion object {
        const val DB_NAME = "photo_comment_db"
    }
}