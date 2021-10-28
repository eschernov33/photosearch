package com.evgenii.photosearch.photolistscreen.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.evgenii.photosearch.photolistscreen.databinding.ItemProgressBinding
import com.evgenii.photosearch.photolistscreen.presentation.adapters.viewholders.PhotosListLoaderStateViewHolder

class PhotosLoadStateAdapter : LoadStateAdapter<PhotosListLoaderStateViewHolder>() {

    override fun onBindViewHolder(holder: PhotosListLoaderStateViewHolder, loadState: LoadState) =
        Unit

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotosListLoaderStateViewHolder =
        PhotosListLoaderStateViewHolder(
            ItemProgressBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
}