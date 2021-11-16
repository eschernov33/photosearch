package com.evgenii.photosearch.detailscreen.presentation.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.evgenii.photosearch.core.presentation.model.NavigateDetailScreenArguments
import com.evgenii.photosearch.detailscreen.presentation.components.AppBar
import com.evgenii.photosearch.detailscreen.presentation.components.Body
import com.evgenii.photosearch.detailscreen.presentation.model.DetailScreenState
import com.evgenii.photosearch.detailscreen.presentation.viewmodel.PhotoDetailViewModel

@Composable
fun DetailScreen(
    navController: NavController,
    args: NavigateDetailScreenArguments,
    viewModel: PhotoDetailViewModel = hiltViewModel(),
) {
    val screenState: State<DetailScreenState> = viewModel.screenState.collectAsState()
    viewModel.onInitScreen(args.photoId)

    Scaffold(
        topBar = {
            AppBar(navController)
        },
        content = {
            Body(args, screenState, viewModel)
        }
    )
}