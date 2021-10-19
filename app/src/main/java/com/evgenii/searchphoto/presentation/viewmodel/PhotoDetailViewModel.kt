package com.evgenii.searchphoto.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evgenii.searchphoto.domain.model.*
import com.evgenii.searchphoto.domain.usecases.GetPhotoByIdUseCase
import com.evgenii.searchphoto.presentation.mapper.PhotoItemMapper
import com.evgenii.searchphoto.presentation.model.PhotoDetailItem
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val getPhotoByIdUseCase: GetPhotoByIdUseCase,
    private val mapper: PhotoItemMapper
) : ViewModel() {

    private var _photoDetail: MutableLiveData<PhotoDetailItem> = MutableLiveData()
    val photoDetail: LiveData<PhotoDetailItem> = _photoDetail

    private var _loadingState: MutableLiveData<LoadResult> = MutableLiveData()
    val loadingState: LiveData<LoadResult> = _loadingState

    init {
        _loadingState.value = EmptyResult
    }

    fun loadDetailInfo(photoId: Int) {
        if (loadingState.value is SuccessResult<*>) return

        _loadingState.value = PendingResult
        val response = getPhotoByIdUseCase(photoId)
        val dispose = response
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isNotEmpty()) {
                    val photoDetailItem = mapper.mapPhotoToPhotoDetailItem(it.first())
                    _photoDetail.value = photoDetailItem
                    _loadingState.value = SuccessResult(photoDetailItem)
                } else {
                    _loadingState.value = EmptyResult
                }
            },
                {
                    _loadingState.value = ErrorResult(it)
                }
            )
    }

}