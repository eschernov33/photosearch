package com.evgenii.searchphoto.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.evgenii.searchphoto.databinding.ItemPhotoBinding
import com.evgenii.searchphoto.presentation.adapters.viewholders.PhotosListViewHolder
import com.evgenii.searchphoto.presentation.model.PhotoItem

class PhotosAdapter(
    private val onItemClick: (photoItem: PhotoItem) -> Unit,
) : PagingDataAdapter<PhotoItem, PhotosListViewHolder>(PhotosListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosListViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PhotosListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotosListViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo, onItemClick)
        }
    }
}