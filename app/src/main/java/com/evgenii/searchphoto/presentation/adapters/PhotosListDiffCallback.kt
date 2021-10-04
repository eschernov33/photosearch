package com.evgenii.searchphoto.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.evgenii.searchphoto.presentation.model.PhotoItem

class PhotosListDiffCallback : DiffUtil.ItemCallback<PhotoItem>() {

    override fun areItemsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: PhotoItem, newItem: PhotoItem): Boolean =
        oldItem.id == newItem.id &&
                oldItem.user == newItem.user &&
                oldItem.downloads == newItem.downloads &&
                oldItem.largeImageURL == newItem.largeImageURL &&
                oldItem.likes == newItem.likes &&
                oldItem.tags == newItem.tags &&
                oldItem.userImageURL == newItem.userImageURL
}