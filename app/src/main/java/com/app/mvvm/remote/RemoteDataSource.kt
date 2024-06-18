package com.app.mvvm.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getData() =
        apiService.getData()
}