package com.evgenii.searchphoto.presentation.adapters.viewholders

import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import com.evgenii.searchphoto.R
import com.evgenii.searchphoto.databinding.ItemPhotoBinding
import com.evgenii.searchphoto.presentation.model.PhotoItem
import com.evgenii.searchphoto.presentation.utils.AnimationUtils
import com.evgenii.searchphoto.presentation.utils.PicassoUtils.Companion.loadFromPicasso

class PhotosListViewHolder(private val binding: ItemPhotoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        photo: PhotoItem?,
        onItemClick: (photo: PhotoItem, extras: FragmentNavigator.Extras) -> Unit,
    ) =
        with(binding) {
            if (photo != null) {
                setPhotoItem(photo)
                root.setOnClickListener {
                    onItemClick(
                        photo,
                        AnimationUtils.getTransitionExtras(ivPhotoCard, ivUserIcon, photo.id)
                    )
                }
            } else {
                setPhotoItemPlaceholder()
                root.setOnClickListener(null)
            }
        }

    private fun ItemPhotoBinding.setPhotoItem(photo: PhotoItem) {
        ViewCompat.setTransitionName(
            ivPhotoCard,
            AnimationUtils.getUniqueTransitionLargePhoto(photo.id)
        )
        ViewCompat.setTransitionName(
            ivUserIcon,
            AnimationUtils.getUniqueTransitionUserPhoto(photo.id)
        )
        tvUserName.text = photo.user
        tvPhotoTag.text = photo.tags
        tvPhotoDownloads.text = photo.downloads
        tvPhotoLikeCount.text = photo.likes
        ivUserIcon.loadFromPicasso(photo.userImageURL, R.drawable.placeholder_avatar)
        ivPhotoCard.loadFromPicasso(
            photo.largeImageURL,
            R.drawable.placeholder_main_image
        )
    }

    private fun ItemPhotoBinding.setPhotoItemPlaceholder() {
        tvUserName.text = ""
        tvPhotoTag.text = ""
        tvPhotoDownloads.text = ""
        tvPhotoLikeCount.text = ""
        ivUserIcon.setImageResource(R.drawable.placeholder_avatar)
        ivPhotoCard.setImageResource(R.drawable.placeholder_main_image)
    }
}