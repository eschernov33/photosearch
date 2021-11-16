package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.evgenii.photosearch.photolistscreen.R
import com.evgenii.photosearch.photolistscreen.domain.usecases.PhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListLoadState
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val mapper: PhotoItemMapper,
    private val photoListUseCase: PhotoListUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(PhotoListScreenState())
    val viewState: StateFlow<PhotoListScreenState> = _viewState

    private var userQuery: String = ""

    fun onSearchTextChanged(query: String) {
        userQuery = query
    }

    fun onSearchButtonClick() {
        startNewSearch()
    }

    fun onLoadingListSuccess() {
        _viewState.value = _viewState.value.copy(
            loadState = PhotoListLoadState.LoadSuccess
        )
    }

    fun onLoadingError() {
        _viewState.value = _viewState.value.copy(
            loadState = PhotoListLoadState.LoadError, errorMessage = R.string.error_load_description
        )
    }

    fun onLoadingEmptyList() {
        _viewState.value = _viewState.value.copy(
            loadState = PhotoListLoadState.LoadEmpty, errorMessage = R.string.error_empty_result
        )
    }

    fun onRetryClick() {
        startNewSearch()
    }

    private fun startNewSearch() {
        _viewState.value = _viewState.value.copy(
            loadState = PhotoListLoadState.Loading,
            errorMessage = R.string.empty,
            photoList = flow {})
        viewModelScope.launch {
            val photoListFlow = photoListUseCase.getPhotos(userQuery)
                .cachedIn(viewModelScope).map { pagingData ->
                    pagingData.map { photo ->
                        mapper.mapPhotoToPhotoItem(photo)
                    }
                }
            _viewState.value = _viewState.value.copy(
                photoList = photoListFlow
            )
        }
    }
}
