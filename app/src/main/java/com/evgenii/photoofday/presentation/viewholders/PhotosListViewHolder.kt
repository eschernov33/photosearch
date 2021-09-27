package com.evgenii.photoofday.presentation.viewholders

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.photoofday.databinding.ItemPhotoBinding
import com.evgenii.photoofday.domain.model.PhotoItem
import com.squareup.picasso.Picasso

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        photo: PhotoItem,
        onItemClick: (photoItem: PhotoItem) -> Unit,
    ) {
        with(binding) {
            tvUserName.text = photo.user
            tvCountryName.text = photo.tags
            tvPopulation.text = photo.downloads.toString()
            tvSquare.text = photo.likes.toString()
            setImage(ivUserIcon, photo.userImageURL)
            setImage(ivCityCard, photo.largeImageURL)
            root.setOnClickListener {
                onItemClick(photo)
            }
        }
    }

    private fun setImage(view: ImageView, url: String) {
        if (url.isNotEmpty())
            Picasso.get().load(url).into(view)
    }
}