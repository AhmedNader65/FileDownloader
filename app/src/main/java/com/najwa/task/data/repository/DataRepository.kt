package com.najwa.task.data.repository

import com.najwa.task.data.dataSource.DataRemoteDataSource
import com.najwa.task.model.FileModel
import com.najwa.task.model.Resource
import com.najwa.task.model.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.ResponseBody
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val dataSource: DataRemoteDataSource
) {
    private val mDataMutex = Mutex()

    private var mData: Resource<ArrayList<FileModel>>? = null

    suspend fun fetchData(): Flow<Resource<ArrayList<FileModel>>?> {
        return flow {
            emit(Resource.loading())
            emit(getDataCached())
            val result = dataSource.getHomeData()
            if (result.status == Status.SUCCESS) {
                result.let {
                    mDataMutex.withLock {
                        mData = it
                    }
                }
            }
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    suspend fun downloadFile(url: String): Flow<Resource<ResponseBody>> {
        return flow {
            emit(dataSource.download(url))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun getDataCached(): Resource<ArrayList<FileModel>>? {
        return mDataMutex.withLock {
            this.mData
        }
    }
}