package com.evgenii.photosearch.photolistscreen.presentation.adapters.viewholders

import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.photosearch.core.presentation.utils.AnimationUtils
import com.evgenii.photosearch.core.presentation.utils.ImageLoaderUtils.Companion.loadFromUrl
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.databinding.ItemPhotoBinding
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(photo: PhotoItem?, onItemClick: (PhotoItem, FragmentNavigator.Extras) -> Unit) =
        if (photo != null) {
            setPhotoItem(photo, onItemClick)
        } else {
            setPhotoItemPlaceholder()
        }

    private fun setPhotoItem(
        photo: PhotoItem,
        onItemClick: (PhotoItem, FragmentNavigator.Extras) -> Unit
    ) =
        with(binding) {
            setTransitionAnimation(photo)
            tvUserName.text = photo.user
            tvPhotoTag.text = photo.tags
            tvPhotoDownloads.text = photo.downloads
            tvPhotoLikeCount.text = photo.likes
            ivUserIcon.loadFromUrl(photo.userImageURL, R.drawable.placeholder_avatar)
            ivPhotoCard.loadFromUrl(photo.largeImageURL, R.drawable.placeholder_main_image)
            ivIconPhotoLike.setOnClickListener(null)
            ivIconDownload.setOnClickListener(null)
            root.setOnClickListener {
                onItemClick(
                    photo,
                    AnimationUtils.getTransitionExtras(ivPhotoCard, ivUserIcon, photo.id)
                )
            }
        }

    private fun setPhotoItemPlaceholder() =
        with(binding) {
            tvUserName.text = ""
            tvPhotoTag.text = ""
            tvPhotoDownloads.text = ""
            tvPhotoLikeCount.text = ""
            ivUserIcon.setImageResource(R.drawable.placeholder_avatar)
            ivPhotoCard.setImageResource(R.drawable.placeholder_main_image)
            root.setOnClickListener(null)
        }

    private fun setTransitionAnimation(photo: PhotoItem) {
        ViewCompat.setTransitionName(
            binding.ivPhotoCard,
            AnimationUtils.getUniqueTransitionLargePhoto(photo.id)
        )
        ViewCompat.setTransitionName(
            binding.ivUserIcon,
            AnimationUtils.getUniqueTransitionUserPhoto(photo.id)
        )
    }
}