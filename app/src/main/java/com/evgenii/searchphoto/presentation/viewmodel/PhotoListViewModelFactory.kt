package com.evgenii.searchphoto.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evgenii.searchphoto.data.api.PhotosApi
import com.evgenii.searchphoto.domain.usecases.LoadPhotoListUseCase
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper

class PhotoListViewModelFactory(
    private val photosApi: PhotosApi,
    private val loadPhotoListUseCase: LoadPhotoListUseCase,
    private val mapper: PhotoItemMapper
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PhotoListViewModel(photosApi, loadPhotoListUseCase, mapper) as T
    }
}