package com.evgenii.photosearch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.evgenii.photosearch.core.presentation.navigation.Route
import com.evgenii.photosearch.detailscreen.presentation.ui.DetailScreen
import com.evgenii.photosearch.photolistscreen.presentation.ui.PhotoListScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val route = Route()

    NavHost(navController = navController, startDestination = route.getPhotoListScreenRoute()) {
        composable(route = route.getPhotoListScreenRoute()) {
            PhotoListScreen(navController = navController)
        }

        composable(route = route.getDetailScreenRoute()) { navBackStackEntry ->
            val args = route.getArgs(navBackStackEntry)
            DetailScreen(navController = navController, args)
        }
    }
}