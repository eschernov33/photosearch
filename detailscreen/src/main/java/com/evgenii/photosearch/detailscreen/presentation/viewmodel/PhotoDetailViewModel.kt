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

    private val _eventShowToastError: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventShowToastError: LiveData<Event<Unit>> = _eventShowToastError

    private val _eventToBackScreen: MutableLiveData<Event<Unit>> = MutableLiveData()
    val eventToBackScreen: LiveData<Event<Unit>> = _eventToBackScreen

    private val _eventOpenInBrowser: MutableLiveData<Event<String>> = MutableLiveData()
    val eventOpenInBrowser: LiveData<Event<String>> = _eventOpenInBrowser

    private var dispose: Disposable? = null

    private val successLoad: (photoList: List<PhotoDetail>) -> Unit = { photoList ->
        if (photoList.isNotEmpty()) {
            _isPhotoLoading.value = false
            val photoDetailItem = mapper.mapPhotoToPhotoDetailItem(photoList.first())
            _photoDetail.value = photoDetailItem
        } else {
            _eventShowToastError.value = Event(Unit)
            _eventToBackScreen.value = Event(Unit)
        }
    }

    private val errorLoad: (throwable: Throwable) -> Unit = { throwable ->
        Timber.e(throwable)
        _eventShowToastError.value = Event(Unit)
        _eventToBackScreen.value = Event(Unit)
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

    fun onOpenInBrowserClick() {
        _photoDetail.value?.let { photoDetailItem ->
            _eventOpenInBrowser.value = Event(photoDetailItem.pageURL)
        }
    }

    fun onBackButtonPressed() {
        _eventToBackScreen.value = Event(Unit)
    }
}