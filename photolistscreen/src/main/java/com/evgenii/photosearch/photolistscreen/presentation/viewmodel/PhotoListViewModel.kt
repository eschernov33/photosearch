package com.evgenii.photosearch.photolistscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.evgenii.photosearch.core.data.api.PhotosApi
import com.evgenii.photosearch.core.data.source.PhotoListPageSource
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.photolistscreen.presentation.mapper.PhotoItemMapper
import com.evgenii.photosearch.photolistscreen.presentation.model.ErrorType
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoItem
import com.evgenii.photosearch.photolistscreen.presentation.model.PhotoListViewsVisibility
import com.evgenii.photosearch.photolistscreen.presentation.model.UserSearch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    private val api: PhotosApi,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var userSearch: UserSearch = UserSearch()

    private var _pagingSource: PhotoListPageSource? = null
        get() {
            if (field == null || field?.invalid == true) {
                _pagingSource = PhotoListPageSource(api, userSearch.query)
            }
            return field
        }
    private val pagingSource: PhotoListPageSource
        get() = _pagingSource ?: throw RuntimeException("Paging source is null")

    val photoListFlow: Flow<PagingData<PhotoItem>> = Pager(pagingConfig) {
        pagingSource
    }.flow
        .cachedIn(viewModelScope).map { pagingData ->
            pagingData.map { photo ->
                mapper.mapPhotoToPhotoItem(photo)
            }
        }

    private var _photoListAreaViewVisibility = MutableLiveData<PhotoListViewsVisibility>()
    val photoListViewsVisibility: LiveData<PhotoListViewsVisibility> =
        _photoListAreaViewVisibility

    private var _errorType = MutableLiveData<ErrorType>()
    val errorMessage: LiveData<ErrorType> = _errorType

    private val _eventShowDetails: MutableLiveData<Event<PhotoItem>> = MutableLiveData()
    val eventShowDetails: LiveData<Event<PhotoItem>> = _eventShowDetails

    private val _eventHideKeyboard: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventHideKeyboard: LiveData<Event<Unit>> = _eventHideKeyboard

    init {
        updatePhotoListAreaViewVisibility()
    }

    fun searchPhotos(query: String) {
        userSearch.query = query
        _pagingSource?.invalidate()
        updatePhotoListAreaViewVisibility(listVisible = true, progressPhotoLoadVisible = true)
        _eventHideKeyboard.value = Event(Unit)
    }

    fun onLoadStateListener(loadState: CombinedLoadStates, itemCount: Int) {
        if (!userSearch.isAlreadySearch) return

        val refreshState = loadState.refresh
        if (refreshState is LoadState.Error) {
            _errorType.value = ErrorType.NETWORK
            updatePhotoListAreaViewVisibility(errorMessageVisible = true, btnRetryVisible = true)
            Timber.d(refreshState.error.localizedMessage)
        } else if (loadState.refresh !is LoadState.Loading) {
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

    fun onRetryClick(query: String) {
        searchPhotos(query)
    }

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

    companion object {
        private const val PAGE_SIZE = 20
        private const val PREFETCH_DISTANCE = 1
        private const val ENABLE_PLACEHOLDER = true
        private const val INITIAL_LOAD_SIZE = 20
        private const val MAX_SIZE = 200

        private val pagingConfig = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = ENABLE_PLACEHOLDER,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE
        )
    }
}
