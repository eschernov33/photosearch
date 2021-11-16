package com.evgenii.photosearch.core.data.datasource

import com.evgenii.photosearch.core.data.model.PhotoApiItem

class DebugData {

    private val photoItem1 = PhotoApiItem(
        downloads = 100,
        id = 1,
        largeImageURL = "https://cdn.pixabay.com/photo/2019/08/19/07/45/dog-4415649_1280.jpg",
        likes = 100,
        tags = "Dog, animal",
        user = "Johnny",
        userImageURL = "https://cdn.pixabay.com/user/2021/06/25/16-51-11-151_250x250.jpg",
        comments = 100,
        views = 18000,
    )

    private val photoItem2 = PhotoApiItem(
        downloads = 300,
        id = 2,
        largeImageURL = "https://cdn.pixabay.com/photo/2016/05/09/10/42/weimaraner-1381186_1280.jpg",
        likes = 800,
        tags = "Dog, animal",
        user = "Fill",
        userImageURL = "https://cdn.pixabay.com/user/2021/10/17/14-08-59-765_250x250.jpg",
        comments = 1032,
        views = 100,
    )

    private val photoItem3 = PhotoApiItem(
        downloads = 8932,
        id = 3,
        largeImageURL = "https://cdn.pixabay.com/photo/2019/07/30/05/53/dog-4372036_1280.jpg",
        likes = 2100,
        tags = "Dog, animal",
        user = "Keil",
        userImageURL = "https://cdn.pixabay.com/user/2021/06/25/16-51-11-151_250x250.jpg",
        comments = 12100,
        views = 112011,
    )

    private val photoItem4 = PhotoApiItem(
        downloads = 23432,
        id = 4,
        largeImageURL = "https://nashzelenyimir.ru/wp-content/uploads/2015/12/%D0%A9%D0%B5%D0%BD%D0%BA%D0%B8-%D0%BB%D0%B0%D0%B1%D1%80%D0%B0%D0%B4%D0%BE%D1%80%D0%B0-%D1%84%D0%BE%D1%82%D0%BE.jpg",
        likes = 3100,
        tags = "For test error",
        user = "Keil",
        userImageURL = "https://cdn.pixabay.com/user/2021/06/25/16-51-11-151_250x250.jpg",
        comments = 1100,
        views = 232012,
    )

    private val debugPhotoItem: MutableList<PhotoApiItem> =
        mutableListOf(
            photoItem1,
            photoItem2,
            photoItem3
        )

    fun getDebugPhotoList(): List<PhotoApiItem> =
        debugPhotoItem.apply { add(photoItem4) }

    fun getDebugPhotoById(id: Int): PhotoApiItem? =
        debugPhotoItem.find { photoApiItem -> photoApiItem.id == id }
}