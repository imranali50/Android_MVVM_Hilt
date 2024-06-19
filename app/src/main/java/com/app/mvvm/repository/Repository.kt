package com.app.mvvm.repository

import com.app.mvvm.model.BaseApiResponse
import com.app.mvvm.model.response.DogResponse
import com.app.mvvm.model.response.MediaUploadResponse
import com.app.mvvm.remote.RemoteDataSource
import com.app.mvvm.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getDog(): Flow<NetworkResult<DogResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.getData() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getVideo(id:String): Flow<NetworkResult<DogResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.downloadVideo(id) })
        }.flowOn(Dispatchers.IO)
    }


    //image
    suspend fun mediaUpload(
        request: RequestBody, file: MultipartBody.Part
    ): Flow<NetworkResult<MediaUploadResponse>> {
        return flow {
            emit(NetworkResult.Loading())
            emit(safeApiCall { remoteDataSource.mediaUpload(request, file) })
        }.flowOn(Dispatchers.IO)
    }
}