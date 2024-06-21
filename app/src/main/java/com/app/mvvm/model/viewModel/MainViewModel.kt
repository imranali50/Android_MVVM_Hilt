package com.app.mvvm.model.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.mvvm.model.request.EventRequest
import com.app.mvvm.model.response.DogResponse
import com.app.mvvm.model.response.EventResponse
import com.app.mvvm.model.response.MediaUploadResponse
import com.app.mvvm.repository.Repository
import com.app.mvvm.util.NetworkResult
import com.app.mvvm.util.UtilJ.getImageType
import com.app.mvvm.util.UtilJ.getRequestTextBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    //simple Get
    private val _response: MutableLiveData<NetworkResult<DogResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<DogResponse>> get() = _response


    fun fetchDogResponse() = viewModelScope.launch {
        repository.getDog().collect { values ->
            _response.value = values
        }
    }

    //Using path Get
    var _videoDownloadResponse: MutableLiveData<NetworkResult<DogResponse>> = MutableLiveData()
    var videoDownloadResponse: LiveData<NetworkResult<DogResponse>> = _videoDownloadResponse

    fun videoDownload(
        id: String
    ) = viewModelScope.launch {
        repository.getVideo(id).collect() {
            _videoDownloadResponse.value = it
        }
    }

    //List api
    var _eventResponse: MutableLiveData<NetworkResult<EventResponse>> = MutableLiveData()
    var eventResponse: LiveData<NetworkResult<EventResponse>> = _eventResponse

    fun eventList(
        eventRequest: EventRequest
    ) = viewModelScope.launch {
        repository.eventList(eventRequest).collect() {
            _eventResponse.value = it
        }
    }

    //mediaUpload
    private val _mediaResponse: MutableLiveData<NetworkResult<MediaUploadResponse>> =
        MutableLiveData()
    val mediaResponse: LiveData<NetworkResult<MediaUploadResponse>> = _mediaResponse

    fun mediaUpload(langType: String,file: MultipartBody.Part) = viewModelScope.launch {
        repository.mediaUpload(
            getRequestTextBody(langType),
           file
        ).collect() {
            _mediaResponse.value = it
        }
    }
}


