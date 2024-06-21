package com.app.mvvm.remote

import com.app.mvvm.model.request.EventRequest
import com.app.mvvm.model.response.MediaUploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {
    suspend fun getData() =
        apiService.getData()

    suspend fun downloadVideo(id: String) =
        apiService.makeVideoDownloadCall(id)

    suspend fun eventList(eventRequest: EventRequest) =
        apiService.eventList(eventRequest)

    //image
    suspend fun mediaUpload(
        request: RequestBody,
        file: MultipartBody.Part
    ): Response<MediaUploadResponse> = apiService.mediaUpload(request, file)

}