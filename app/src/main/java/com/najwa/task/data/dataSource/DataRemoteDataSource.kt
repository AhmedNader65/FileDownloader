package com.najwa.task.data.dataSource

import com.najwa.task.data.api.DataApi
import com.najwa.task.getResponse
import com.najwa.task.model.FileModel
import com.najwa.task.model.Resource
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

}