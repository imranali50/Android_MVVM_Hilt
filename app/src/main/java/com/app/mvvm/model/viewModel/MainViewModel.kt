package com.app.mvvm.model.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.mvvm.model.response.DogResponse
import com.app.mvvm.model.response.MediaUploadResponse
import com.app.mvvm.repository.Repository
import com.app.mvvm.util.NetworkResult
import com.app.mvvm.util.UtilJ.getFileType
import com.app.mvvm.util.UtilJ.getRequestTextBody
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

    //mediaUpload
    private val _mediaResponse: MutableLiveData<NetworkResult<MediaUploadResponse>> =
        MutableLiveData()
    val mediaResponse: LiveData<NetworkResult<MediaUploadResponse>> = _mediaResponse

    fun mediaUpload(imageType: String, file: File, imageOrVide: Int) = viewModelScope.launch {
        repository.mediaUpload(
            getRequestTextBody(imageType),
           getFileType(file, imageOrVide)
        ).collect() {
            _mediaResponse.value = it
        }
    }
}


