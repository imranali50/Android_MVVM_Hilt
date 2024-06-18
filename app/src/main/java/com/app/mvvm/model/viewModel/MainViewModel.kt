package com.app.mvvm.model.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.mvvm.model.DogResponse
import com.app.mvvm.repository.Repository
import com.app.mvvm.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: Repository
) : AndroidViewModel(application) {

    private val _response: MutableLiveData<NetworkResult<DogResponse>> = MutableLiveData()
    val response: LiveData<NetworkResult<DogResponse>> get() = _response

    fun fetchDogResponse() = viewModelScope.launch {
        repository.getDog().collect { values ->
            _response.value = values
        }
    }
}


