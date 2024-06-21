package com.app.mvvm.util

import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

object UtilJ {

    fun getImageType(fileImage: File): MultipartBody.Part {
        val requestBody = RequestBody.create("*/*".toMediaTypeOrNull(), fileImage)
        Log.e("file name", "uploadMedia: $fileImage")
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("files", fileImage.name, requestBody)

        return body
    }

    fun getRequestTextBody(type: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), type)
    }

    //    fun getFileType(fileImage: File, type: Int): Part {
//        val requestBody = RequestBody.create(
//            parse.parse(if ((type == 2)) "image/jpeg" else if ((type == 3)) "video/mp4" else "audio/mpeg"),
//            fileImage
//        )
//        return createFormData.createFormData("file", fileImage.name, requestBody)
//    }

}
