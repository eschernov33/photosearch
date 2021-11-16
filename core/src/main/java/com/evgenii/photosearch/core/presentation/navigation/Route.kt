package com.evgenii.photosearch.core.presentation.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.evgenii.photosearch.core.presentation.model.NavigateDetailScreenArguments

class Route {

    fun getDetailScreenRoute(): String =
        "$NAV_DESTINATION_PHOTO_DETAIL_SCREEN?$ARG_PHOTO_ID={$ARG_PHOTO_ID}&$ARG_IMAGE_URL={$ARG_IMAGE_URL}"

    fun getPhotoListScreenRoute(): String =
        NAV_DESTINATION_PHOTO_LIST_SCREEN

    fun navigateToDetailScreen(navController: NavController, photoId: Int, photoUrl: String) =
        navController.navigate("$NAV_DESTINATION_PHOTO_DETAIL_SCREEN?$ARG_PHOTO_ID=$photoId&$ARG_IMAGE_URL=$photoUrl")

    fun getArgs(navBackStackEntry: NavBackStackEntry): NavigateDetailScreenArguments =
        NavigateDetailScreenArguments(
            requireNotNull(navBackStackEntry.arguments?.getString(ARG_PHOTO_ID)).toInt(),
            requireNotNull(navBackStackEntry.arguments?.getString(ARG_IMAGE_URL))
        )

    companion object {
        const val NAV_DESTINATION_PHOTO_LIST_SCREEN = "photo_list_screen"
        const val NAV_DESTINATION_PHOTO_DETAIL_SCREEN = "photo_detail_screen"

        const val ARG_PHOTO_ID = "photo_id"
        const val ARG_IMAGE_URL = "image_url"
    }
}