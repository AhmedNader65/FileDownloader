package com.najwa.task.data.api

import com.najwa.task.model.FileModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DataApi {

    @GET("fetch-files")
    suspend fun fetchFiles(): Response<ArrayList<FileModel>>

    @GET
    @Streaming
    suspend fun download(@Url url: String): Response<ResponseBody>


}