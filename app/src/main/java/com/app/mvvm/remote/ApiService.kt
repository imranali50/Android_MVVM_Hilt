package com.app.mvvm.remote

import com.app.mvvm.model.request.EventRequest
import com.app.mvvm.model.response.DogResponse
import com.app.mvvm.model.response.EventResponse
import com.app.mvvm.model.response.MediaUploadResponse
import com.app.mvvm.util.Constant
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
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
    @GET("")
    suspend fun makeVideoDownloadCall(@Path("id") id: String): Response<DogResponse>


 //Using Path
    @POST("")
    suspend fun eventList(@Body eventRequest: EventRequest): Response<EventResponse>

    //MultiPartFile
    @Multipart
    @POST("")
    suspend fun mediaUpload(
        @Part("langType") fullName: RequestBody?, @Part files: MultipartBody.Part
    ): Response<MediaUploadResponse>

}