package com.evgenii.photosearch.photolistscreen.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.evgenii.photosearch.core.presentation.navigation.Route
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.presentation.components.PhotoListItem
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isLoadEmpty
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isLoadError
import com.evgenii.photosearch.photolistscreen.presentation.extensions.isNotLoadingProcess
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListScreenState
import com.evgenii.photosearch.photolistscreen.presentation.viewmodel.PhotoListViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Composable
fun PhotoListScreen(
    navController: NavController,
    viewModel: PhotoListViewModel = hiltViewModel()
) {

    val screenState: State<PhotoListScreenState> = viewModel.viewState.collectAsState()

    Column(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Header(viewModel)
        PhotoList(
            isListVisible = screenState.value.isListVisible,
            photoListFlow = screenState.value.photoList,
            viewModel = viewModel,
            navController = navController
        )
        LoadingProgress(screenState.value.isSearchLoadingProgressVisible)
        ErrorMessage(
            isVisible = screenState.value.isErrorVisible,
            res = screenState.value.errorMessage
        )
        RetryButton(isVisible = screenState.value.isRetryButtonVisible) {
            viewModel.onRetryClick()
        }
    }
}

@Composable
fun RetryButton(isVisible: Boolean, onRetryClick: () -> Unit) {
    if (isVisible) {
        Button(onClick = onRetryClick) {
            Text(text = stringResource(id = R.string.retry_search))
        }
    }
}

@Composable
fun LoadingProgress(searchLoadingProgressVisible: Boolean) {
    if (searchLoadingProgressVisible) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
fun Header(viewModel: PhotoListViewModel) {
    Logo()
    Search(viewModel::onSearchTextChanged) {
        viewModel.onSearchButtonClick()
    }
}

@Composable
fun Search(onSearchTextChanged: (query: String) -> Unit, onSearchButtonClick: () -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onSearchTextChanged(it)
        },
        label = { Text(stringResource(id = R.string.hint_photo_search)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 4.dp),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        singleLine = true,
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearchButtonClick()
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun PhotoList(
    isListVisible: Boolean,
    photoListFlow: Flow<PagingData<PhotoItem>>,
    viewModel: PhotoListViewModel,
    navController: NavController
) {
    val lazyPagingPhoto: LazyPagingItems<PhotoItem> = photoListFlow.collectAsLazyPagingItems()

    lazyPagingPhoto.apply {
        val refreshState: LoadState = loadState.refresh
        if (refreshState.isNotLoadingProcess()) {
            when {
                refreshState.isLoadError() -> {
                    viewModel.onLoadingError()
                    Timber.d((refreshState as LoadState.Error).error.localizedMessage)
                }
                loadState.isLoadEmpty(itemCount) -> {
                    viewModel.onLoadingEmptyList()
                }
                itemCount > 0 -> {
                    viewModel.onLoadingListSuccess()
                }
            }
        }
    }
    if (lazyPagingPhoto.itemCount > 0 && isListVisible) {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .width(400.dp)
        ) {
            itemsIndexed(lazyPagingPhoto) { _, photoItem ->
                PhotoListItem(photoItem) {
                    photoItem?.let {
                        Route().navigateToDetailScreen(
                            navController,
                            photoItem.id,
                            photoItem.largeImageURL
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun ErrorMessage(isVisible: Boolean, res: Int) {
    if (isVisible) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = res), textAlign = TextAlign.Center,
                color = MaterialTheme.colors.error
            )
        }
    }
}

@Composable
fun Logo() {
    Image(
        painter = painterResource(R.drawable.logo_photo_search),
        contentDescription = stringResource(R.string.content_description_logo),
        modifier = Modifier
            .width(200.dp)
            .padding(top = 10.dp),

        )
}

@Preview(showBackground = true)
@Composable
fun PreviewPhotoListScreen() {
}
