package com.evgenii.photosearch.detailscreen.presentation.components

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.evgenii.photosearch.R
import com.evgenii.photosearch.core.presentation.model.NavigateDetailScreenArguments
import com.evgenii.photosearch.detailscreen.presentation.model.DetailLoadState
import com.evgenii.photosearch.detailscreen.presentation.model.DetailScreenState
import com.evgenii.photosearch.detailscreen.presentation.model.PhotoDetailItem
import com.evgenii.photosearch.detailscreen.presentation.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import moe.tlaster.nestedscrollview.VerticalNestedScrollView
import moe.tlaster.nestedscrollview.rememberNestedScrollViewState

@Composable
fun Body(
    args: NavigateDetailScreenArguments,
    screenState: State<DetailScreenState>,
    viewModel: PhotoDetailViewModel
) {
    val nestedScrollViewState = rememberNestedScrollViewState()

    VerticalNestedScrollView(
        state = nestedScrollViewState,
        header = {
            HeaderImage(args)
        },
        content = {
            BodyContent(screenState, viewModel::onSaveCommentClick, viewModel::onRetryClick)
        }
    )
}

@Composable
private fun HeaderImage(args: NavigateDetailScreenArguments) {
    Image(
        painter = rememberImagePainter(
            data = args.photoUrl,
            builder = {
                placeholder(R.drawable.placeholder_main_image)
                size(OriginalSize)
            }
        ),
        contentDescription = stringResource(id = R.string.content_description_main_image),
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
private fun BodyContent(
    screenState: State<DetailScreenState>,
    onSaveCommentClick: (newComment: String) -> Unit,
    onRetryClick: () -> Unit
) {
    val photoDetail: PhotoDetailItem = screenState.value.photoDetail ?: photoDetailPlaceholder

    Column {
        PhotoInfo(photoDetail)
        Divider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 10.dp, start = 10.dp)
        )
        PhotoDescription(screenState, onRetryClick, photoDetail)
        if (screenState.value.loadState == DetailLoadState.LoadSuccess) {
            Comment(screenState.value.comment) {
                onSaveCommentClick(it)
            }
        }
    }
}

@Composable
private fun PhotoDescription(
    screenState: State<DetailScreenState>,
    onRetryClick: () -> Unit,
    photoDetail: PhotoDetailItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        when {
            screenState.value.isLoadingProgressVisible -> {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colors.primary
                    )
                }
            }
            screenState.value.isErrorVisible -> {
                Text(
                    text = stringResource(id = R.string.error_load_detail),
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        onRetryClick()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = stringResource(id = R.string.retry))
                }
            }
            else -> {
                Text(
                    text = stringResource(id = R.string.title_information),
                    style = MaterialTheme.typography.subtitle2
                )
                CombinedTextField(R.string.title_views, photoDetail.views)
                CombinedTextField(R.string.title_loads, photoDetail.downloads)
                CombinedTextField(R.string.title_likes, photoDetail.likes)
                CombinedTextField(R.string.title_comments, photoDetail.comments)
            }
        }
    }
}

@Composable
private fun PhotoInfo(photoDetail: PhotoDetailItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberImagePainter(
                data = photoDetail.userImageURL,
                builder = {
                    placeholder(R.drawable.placeholder_avatar)
                }),
            contentDescription = stringResource(
                id = R.string.content_description_main_image
            ),
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape)
        )
        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text(text = photoDetail.user, style = MaterialTheme.typography.h6)
            Text(text = photoDetail.tags, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun Comment(comment: String, onClickSaveComment: (newComment: String) -> Unit) {
    var currentComment by remember {
        mutableStateOf(comment)
    }
    var value by remember {
        mutableStateOf(comment)
    }
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        value = value,
        onValueChange = {
            value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        label = {
            Text(text = stringResource(id = R.string.label_comment))
        },
    )

    if (currentComment != value) {
        Button(
            onClick = {
                onClickSaveComment(value)
                focusManager.clearFocus()
                currentComment = value
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = stringResource(id = R.string.btn_save))
        }
    }
}

@Composable
private fun CombinedTextField(@StringRes title: Int, value: String) =
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(text = stringResource(id = title), style = MaterialTheme.typography.subtitle2)
        Text(
            text = value,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 8.dp)
        )
    }

@Preview
@Composable
fun Preview() =
    BodyContent(
        screenState = MutableStateFlow(
            DetailScreenState(
                loadState = DetailLoadState.LoadSuccess,
                photoDetail =
                PhotoDetailItem(
                    downloads = "100",
                    id = 1,
                    likes = "100",
                    tags = "Dog, animal",
                    user = "Johnny",
                    userImageURL = "https://cdn.pixabay.com/user/2021/06/25/16-51-11-151_250x250.jpg",
                    comments = "100",
                    views = "18000",
                )
            )
        ).collectAsState(), {}, {})

private val photoDetailPlaceholder = PhotoDetailItem(
    1,
    "",
    "",
    "",
    "",
    "",
    "",
    "",
)