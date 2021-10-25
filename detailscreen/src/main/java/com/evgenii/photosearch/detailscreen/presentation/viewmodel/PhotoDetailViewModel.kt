package com.evgenii.photosearch.detailscreen.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evgenii.photosearch.core.domain.model.PhotoDetail
import com.evgenii.photosearch.core.presentation.model.Event
import com.evgenii.photosearch.core.presentation.model.PhotoDetailItem
import com.evgenii.photosearch.detailscreen.domain.usecases.GetPhotoByIdUseCase
import com.evgenii.photosearch.detailscreen.presentation.mapper.PhotoItemMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class PhotoDetailViewModel @Inject constructor(
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoDetail: MutableLiveData<PhotoDetailItem> = MutableLiveData()
    val photoDetail: LiveData<PhotoDetailItem> = _photoDetail

    private var _isPhotoLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isPhotoLoading: LiveData<Boolean> = _isPhotoLoading

    private val _showToastError: MutableLiveData<Event<Unit>> = MutableLiveData()
    val showToastError: LiveData<Event<Unit>> = _showToastError

    private val _navigateToBackScreen: MutableLiveData<Event<Unit>> = MutableLiveData()
    val navigateToBackScreen: LiveData<Event<Unit>> = _navigateToBackScreen

    private val _openInBrowser: MutableLiveData<Event<String>> = MutableLiveData()
    val openInBrowser: LiveData<Event<String>> = _openInBrowser

    private var dispose: Disposable? = null

    private val successLoad: (photoList: List<PhotoDetail>) -> Unit = { photoList ->
        if (photoList.isNotEmpty()) {
            _isPhotoLoading.value = false
            val photoDetailItem = mapper.mapPhotoToPhotoDetailItem(photoList.first())
            _photoDetail.value = photoDetailItem
        } else {
            _showToastError.value = Event(Unit)
            _navigateToBackScreen.value = Event(Unit)
        }
    }

    private val errorLoad: (throwable: Throwable) -> Unit = { throwable ->
        Timber.e(throwable)
        _showToastError.value = Event(Unit)
        _navigateToBackScreen.value = Event(Unit)
    }

    fun loadDetailInfo(photoId: Int) {
        if (photoDetail.value == null) {
            _isPhotoLoading.value = true
            dispose = getPhotoByIdUseCase(photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(successLoad, errorLoad)
        }
    }

    fun onOpenInBrowserClick() =
        _photoDetail.value?.let { photoDetailItem ->
            _openInBrowser.value = Event(photoDetailItem.pageURL)
        }

    fun onBackButtonPressed() {
        _navigateToBackScreen.value = Event(Unit)
    }
}