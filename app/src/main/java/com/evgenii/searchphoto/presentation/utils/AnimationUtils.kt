package com.evgenii.searchphoto.presentation.utils

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras

object AnimationUtils {

    private const val TRANSITION_NAME_PREFIX = "imageHeader_"
    private const val TRANSITION_NAME_AVATAR_PREFIX = "imageAvatar_"

    fun getUniqueTransitionName(cityId: Int): String  {
        return "$TRANSITION_NAME_PREFIX$cityId"
    }

    fun getUniqueTransitionNameAvatar(cityId: Int): String  {
        return "$TRANSITION_NAME_AVATAR_PREFIX$cityId"
    }

    fun getTransitionExtras(view: View, cityId: Int) =
        FragmentNavigatorExtras(view to getUniqueTransitionName(cityId))

    fun getTransitionExtrasAvatar(view: View, cityId: Int) =
        FragmentNavigatorExtras(view to getUniqueTransitionNameAvatar(cityId))
}