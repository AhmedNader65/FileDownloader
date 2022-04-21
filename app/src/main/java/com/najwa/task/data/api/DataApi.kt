package com.najwa.task.data.api

import com.najwa.task.model.Status
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface DataApi {

    @GET("files")
    suspend fun fetchFiles(
        @Header("Authorization") token: String?,
    ): Response<Status>


}