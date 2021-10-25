package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.cachedIn
import androidx.paging.rxjava2.observable
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.domain.usecases.GetPhotoListUseCase
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorType
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListViewsVisibility
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val getPhotoListUseCase: GetPhotoListUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoList = MutableLiveData<PagingData<PhotoItem>>()
    val photoList: LiveData<PagingData<PhotoItem>>
        get() = _photoList

    private var _photoListAreaViewVisibility = MutableLiveData<PhotoListViewsVisibility>()
    val photoListViewsVisibility: LiveData<PhotoListViewsVisibility> =
        _photoListAreaViewVisibility

    private var _errorType = MutableLiveData<ErrorType>()
    val errorMessage: LiveData<ErrorType> = _errorType

    private val _eventShowDetails: MutableLiveData<Event<PhotoItem>> = MutableLiveData()
    val eventShowDetails: LiveData<Event<PhotoItem>> = _eventShowDetails

    private val _eventHideKeyboard: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventHideKeyboard: LiveData<Event<Unit>> = _eventHideKeyboard

    private var disposable: Disposable? = null

    init {
        updatePhotoListAreaViewVisibility()
    }

    @ExperimentalCoroutinesApi
    fun searchPhotos(query: String) {
        updatePhotoListAreaViewVisibility(listVisible = true, progressPhotoLoadVisible = true)
        _eventHideKeyboard.value = Event(Unit)
        val pager = getPhotoListUseCase(query)
        disposable = pager.observable
            .map { pagingData ->
                pagingData.map { photo ->
                    mapper.mapPhotoToPhotoItem(photo)
                }
            }.cachedIn(viewModelScope).subscribe({ pagingData ->
                _photoList.value = pagingData
            }, {
                Timber.d(it)
            })
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        val refreshState = loadState.refresh
        if (refreshState.isLoadError()) {
            _errorType.value = ErrorType.NETWORK
            updatePhotoListAreaViewVisibility(errorMessageVisible = true, btnRetryVisible = true)
            Timber.d((refreshState as LoadState.Error).error.localizedMessage)
        } else if (refreshState.isNotLoading()) {
            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached && itemCount < 1
            ) {
                updatePhotoListAreaViewVisibility(errorMessageVisible = true)
                _errorType.value = ErrorType.NOT_FOUND
            } else if (itemCount > 0) {
                updatePhotoListAreaViewVisibility(listVisible = true)
            }
        }
    }

    fun onPhotoDetails(photoItem: PhotoItem) {
        _eventShowDetails.value = Event(photoItem)
    }

    @ExperimentalCoroutinesApi
    fun onRetryClick(query: String) {
        searchPhotos(query)
    }

    private fun LoadState.isLoadError(): Boolean =
        this is LoadState.Error

    private fun LoadState.isNotLoading(): Boolean =
        this !is LoadState.Loading

    private fun updatePhotoListAreaViewVisibility(
        listVisible: Boolean = false,
        progressPhotoLoadVisible: Boolean = false,
        errorMessageVisible: Boolean = false,
        btnRetryVisible: Boolean = false
    ) {
        _photoListAreaViewVisibility.value = PhotoListViewsVisibility(
            listVisible = listVisible,
            progressPhotoLoadVisible = progressPhotoLoadVisible,
            errorMessageVisible = errorMessageVisible,
            btnRetryVisible = btnRetryVisible
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}
