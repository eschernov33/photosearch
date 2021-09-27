package com.evgenii.photoofday.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.evgenii.photoofday.databinding.ItemPhotoBinding
import com.evgenii.photoofday.domain.model.PhotoItem
import com.evgenii.photoofday.presentation.viewholders.PhotosListViewHolder

class PhotosAdapter(
    private val onItemClick: (photoItem: PhotoItem) -> Unit,
) : PagedListAdapter<PhotoItem, PhotosListViewHolder>(PhotosListDiffCallback()) {

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
        if (photo != null)
            holder.bind(photo, onItemClick)
    }
}