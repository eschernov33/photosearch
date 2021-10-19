package com.evgenii.searchphoto.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import com.evgenii.searchphoto.databinding.ItemProgressBinding
import com.evgenii.searchphoto.presentation.adapters.viewholders.PhotosListLoaderStateViewHolder

class PhotosAdapterLoadState: LoadStateAdapter<PhotosListLoaderStateViewHolder>() {

    override fun onBindViewHolder(holder: PhotosListLoaderStateViewHolder, loadState: LoadState) = Unit

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): PhotosListLoaderStateViewHolder {
        return PhotosListLoaderStateViewHolder(ItemProgressBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}