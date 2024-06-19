package com.app.mvvm.remote

import com.app.mvvm.model.response.DogResponse
import com.app.mvvm.model.response.MediaUploadResponse
import com.app.mvvm.util.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    //Simple Get
    @GET(Constant.RANDOM_URL)
    suspend fun getData(): Response<DogResponse>

    //Using Path
    @GET("talk-video/job-status/{id}")
    suspend fun makeVideoDownloadCall(@Path("id") id: String): Response<DogResponse>

    //MultiPartFile
    @Multipart
    @POST("upload")
    suspend fun mediaUpload(
        @Part("uploadType") fullName: RequestBody?, @Part file: MultipartBody.Part
    ): Response<MediaUploadResponse>

}