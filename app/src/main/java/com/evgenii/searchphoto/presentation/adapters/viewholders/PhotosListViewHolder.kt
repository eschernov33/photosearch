package com.evgenii.searchphoto.presentation.adapters.viewholders

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.searchphoto.databinding.ItemPhotoBinding
import com.evgenii.searchphoto.domain.model.PhotoItem
import com.squareup.picasso.Picasso

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        photo: PhotoItem,
        onItemClick: (photoItem: PhotoItem) -> Unit,
    ) {
        with(binding) {
            tvUserName.text = photo.user
            tvPhotoTag.text = photo.tags
            tvPhotoDownloads.text = photo.downloads.toString()
            tvPhotoLikeCount.text = photo.likes.toString()
            setImage(ivUserIcon, photo.userImageURL)
            setImage(ivPhotoCard, photo.largeImageURL)
            root.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    private fun setImage(view: ImageView, url: String) {
        if (url.isNotEmpty()) {
            Picasso.get().load(url).into(view)
        }
    }
}