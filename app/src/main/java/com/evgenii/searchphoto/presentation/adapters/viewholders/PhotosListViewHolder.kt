package com.evgenii.searchphoto.presentation.adapters.viewholders

import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.searchphoto.databinding.ItemPhotoBinding
import com.evgenii.searchphoto.presentation.model.PhotoItem
import com.evgenii.searchphoto.presentation.utils.AnimationUtils
import com.squareup.picasso.Picasso

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        photo: PhotoItem,
        onItemClick: (photo: PhotoItem, extras: FragmentNavigator.Extras) -> Unit,
    ) {
        with(binding) {
            ViewCompat.setTransitionName(
                ivPhotoCard,
                AnimationUtils.getUniqueTransitionName(photo.id)
            )
            ViewCompat.setTransitionName(
                ivUserIcon,
                AnimationUtils.getUniqueTransitionNameAvatar(photo.id)
            )
            tvUserName.text = photo.user
            tvPhotoTag.text = photo.tags
            tvPhotoDownloads.text = photo.downloads
            tvPhotoLikeCount.text = photo.likes
            ivUserIcon.loadImage(photo.userImageURL)
            ivPhotoCard.loadImage(photo.largeImageURL)
            root.setOnClickListener {
                onItemClick(photo, getTransitionExtras(ivPhotoCard, ivUserIcon, photo.id))
            }
        }
    }

    private fun ImageView.loadImage(url: String) {
        if (url.isNotEmpty()) {

            Picasso.get().load(url).into(this)
        }
    }

    private fun getTransitionExtras(view: View, view2: View, id: Int): FragmentNavigator.Extras {
        val transitionImage = AnimationUtils.getUniqueTransitionName(id)
        val transitionAvatar = AnimationUtils.getUniqueTransitionNameAvatar(id)
        return FragmentNavigatorExtras(view to transitionImage, view2 to transitionAvatar)
    }
}