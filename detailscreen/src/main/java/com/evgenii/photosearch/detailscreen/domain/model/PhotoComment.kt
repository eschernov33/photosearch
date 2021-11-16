package com.evgenii.photosearch.detailscreen.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PhotoComment(
    @PrimaryKey val photoId: Int,
    val comment: String
)