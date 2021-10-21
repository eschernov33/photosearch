package com.evgenii.photosearch.photolistscreen.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigator
import androidx.paging.PagingDataAdapter
import com.evgenii.photosearch.photolistscreen.databinding.ItemPhotoBinding
import com.evgenii.photosearch.photolistscreen.presentation.adapters.viewholders.PhotosListViewHolder
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem

class PhotosAdapter(
    private val onItemClick: (photoItem: PhotoItem, extras: FragmentNavigator.Extras) -> Unit,
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
        holder.bind(photo, onItemClick)
    }
}