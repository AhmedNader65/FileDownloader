package com.najwa.task.data.dataSource

import com.najwa.task.data.api.DataApi
import com.najwa.task.getResponse
import com.najwa.task.model.FileModel
import com.najwa.task.model.Resource
import okhttp3.ResponseBody
import javax.inject.Inject

class DataRemoteDataSource @Inject constructor(
    private val dataApi: DataApi
) {

    suspend fun getHomeData(): Resource<ArrayList<FileModel>> {
        return getResponse(
            request = { dataApi.fetchFiles() },
            defaultErrorMessage = "Error fetching data"
        )
    }

    suspend fun download(url: String): Resource<ResponseBody> {
        return getResponse(
            request = { dataApi.download(url) },
            defaultErrorMessage = "Error downloading file"
        )

    }


}