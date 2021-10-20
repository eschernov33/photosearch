package com.evgenii.searchphoto.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evgenii.searchphoto.domain.model.PhotoDetail
import com.evgenii.searchphoto.domain.usecases.GetPhotoByIdUseCase
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper
import com.evgenii.searchphoto.presentation.model.Event
import com.evgenii.searchphoto.presentation.model.PhotoDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoDetail: MutableLiveData<PhotoDetailItem> = MutableLiveData()
    val photoDetail: LiveData<PhotoDetailItem> = _photoDetail

    private var _loadingProgressVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val loadingProgressVisibility: LiveData<Boolean> = _loadingProgressVisibility

    private val _actionShowToastError: MutableLiveData<Event<Unit>> = MutableLiveData()
    val actionShowToastError: LiveData<Event<Unit>> = _actionShowToastError

    private val _actionToBackScreen: MutableLiveData<Event<Unit>> = MutableLiveData()
    val actionToBackScreen: LiveData<Event<Unit>> = _actionToBackScreen

    private val _actionOpenInBrowser: MutableLiveData<Event<String>> = MutableLiveData()
    val actionOpenInBrowser: LiveData<Event<String>> = _actionOpenInBrowser

    private var dispose: Disposable? = null

    private val successLoad: (photoList: List<PhotoDetail>) -> Unit = { photoList ->
        if (photoList.isNotEmpty()) {
            _loadingProgressVisibility.value = false
            val photoDetailItem = mapper.mapPhotoToPhotoDetailItem(photoList.first())
            _photoDetail.value = photoDetailItem
        } else {
            _actionShowToastError.value = Event(Unit)
            _actionToBackScreen.value = Event(Unit)
        }
    }

    private val errorLoad: (throwable: Throwable) -> Unit = { throwable ->
        Timber.e(throwable)
        _actionShowToastError.value = Event(Unit)
        _actionToBackScreen.value = Event(Unit)
    }

    fun loadDetailInfo(photoId: Int) {
        if (photoDetail.value == null) {
            _loadingProgressVisibility.value = true
            dispose = getPhotoByIdUseCase(photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(successLoad, errorLoad)
        }
    }

    fun onOpenInBrowserClick() {
        _photoDetail.value?.let { photoDetailItem ->
            _actionOpenInBrowser.value = Event(photoDetailItem.pageURL)
        }
    }
}