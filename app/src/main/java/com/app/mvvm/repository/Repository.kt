package com.app.mvvm.repository

import com.app.mvvm.model.BaseApiResponse
import com.app.mvvm.model.DogResponse
import com.app.mvvm.remote.RemoteDataSource
import com.app.mvvm.util.NetworkResult
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getDog(): Flow<NetworkResult<DogResponse>> {
        return flow {
            emit(safeApiCall { remoteDataSource.getData() })
        }.flowOn(Dispatchers.IO)
    }
}