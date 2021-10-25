package com.evgenii.photosearch.core.presentation.utils

import android.view.View
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras

class AnimationUtils {

    companion object {
        private const val TRANSITION_PHOTO_HEADER_PREFIX = "photoHeader_"
        private const val TRANSITION_USER_IMAGE_PREFIX = "userImage_"

        fun getUniqueTransitionLargePhoto(photoId: Int): String =
            "$TRANSITION_PHOTO_HEADER_PREFIX$photoId"

        fun getUniqueTransitionUserPhoto(photoId: Int): String =
            "$TRANSITION_USER_IMAGE_PREFIX$photoId"

        fun getTransitionExtras(viewLargePhoto: View, viewUserImage: View, id: Int):
                FragmentNavigator.Extras {
            val transitionLargePhoto: String = getUniqueTransitionLargePhoto(id)
            val transitionUserPhoto: String = getUniqueTransitionUserPhoto(id)
            return FragmentNavigatorExtras(
                viewLargePhoto to transitionLargePhoto,
                viewUserImage to transitionUserPhoto
            )
        }
    }
}