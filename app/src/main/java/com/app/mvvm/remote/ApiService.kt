package com.app.mvvm.remote

import com.app.mvvm.model.DogResponse
import com.app.mvvm.util.Constant
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constant.RANDOM_URL)
    suspend fun getData(): Response<DogResponse>

}