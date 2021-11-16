package com.evgenii.photosearch.photolistscreen.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem

@Composable
fun PhotoListItem(
    photoItem: PhotoItem?,
    onClickItem: (() -> Unit)?
) {
    Card(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .clickable {
            onClickItem?.let { onClickItem() }
        }) {
        Column {
            PhotoItemHeader(photoItem)
            PhotoItemMainImage(photoItem?.largeImageURL)
            PhotoItemFooter(photoItem)
        }
    }
}

@Composable
fun PhotoItemFooter(photoItem: PhotoItem?) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                painter = painterResource(id = R.drawable.ic_download),
                contentDescription = stringResource(
                    id = R.string.content_description_icon_download
                ),
                modifier = Modifier
                    .size(20.dp)
            )
            photoItem?.downloads?.let { downloads ->
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = downloads,
                    style = MaterialTheme.typography.body2
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Image(
                painter = painterResource(id = R.drawable.ic_like),
                contentDescription = stringResource(
                    id = R.string.content_description_icon_likes
                ),
                modifier = Modifier
                    .size(20.dp)
            )
            photoItem?.likes?.let { likes ->
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = likes,
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}

@Composable
private fun PhotoItemHeader(photoItem: PhotoItem?) {
    Row(modifier = Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = rememberImagePainter(
                photoItem?.userImageURL,
                builder = {
                    placeholder(R.drawable.placeholder_avatar)
                }
            ),
            contentDescription = stringResource(R.string.content_description_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Gray, CircleShape),
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        ) {
            photoItem?.user?.let { userName ->
                Text(text = userName, style = MaterialTheme.typography.h6)
            }
            photoItem?.tags?.let { tags ->
                Text(text = tags, style = MaterialTheme.typography.body2)
            }

        }
    }
}

@Composable
private fun PhotoItemMainImage(imageUrl: String?) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = rememberImagePainter(
                imageUrl,
                builder = {
                    placeholder(R.drawable.placeholder_main_image)
                }
            ),
            contentDescription = stringResource(
                id = R.string.content_description_photo
            ),
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
    }
}

@Preview
@Composable
fun PreviewPhotoListItem() {
    PhotoListItem(
        photoItem = PhotoItem(
            id = 1,
            user = "Johnny",
            userImageURL = "",
            likes = "10",
            downloads = "2000",
            largeImageURL = "",
            tags = "tags"
        ), onClickItem = {}
    )
}