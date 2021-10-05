package com.evgenii.searchphoto.presentation.adapters.viewholders

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.searchphoto.databinding.ItemPhotoBinding
import com.evgenii.searchphoto.presentation.model.PhotoItem
import com.squareup.picasso.Picasso

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        photo: PhotoItem,
        onItemClick: (photo: PhotoItem) -> Unit,
    ) {
        with(binding) {
            tvUserName.text = photo.user
            tvPhotoTag.text = photo.tags
            tvPhotoDownloads.text = photo.downloads
            tvPhotoLikeCount.text = photo.likes
            ivUserIcon.loadImage(photo.userImageURL)
            ivPhotoCard.loadImage(photo.largeImageURL)
            root.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    private fun ImageView.loadImage(url: String) {
        if (url.isNotEmpty()) {
            Picasso.get().load(url).into(this)
        }
    }
}