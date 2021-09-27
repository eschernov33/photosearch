package com.evgenii.searchphoto.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.evgenii.searchphoto.domain.model.PhotoItem

class PhotosListDiffCallback : DiffUtil.ItemCallback<PhotoItem>() {

    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean {
        return oldItem == newItem
    }
}