package com.najwa.task.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.najwa.task.data.repository.DataRepository
import com.najwa.task.model.DownloadStatus
import com.najwa.task.model.FileModel
import com.najwa.task.model.Resource
import com.najwa.task.model.Status
import com.najwa.task.writeResponseBodyToDisk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private var fetchDataJob: Job? = null

    private val _data = MutableStateFlow(Resource<ArrayList<FileModel>>(Status.INIT, null, null))
    val data: StateFlow<Resource<ArrayList<FileModel>>> = _data.asStateFlow()

    private val _downloading: MutableStateFlow<DownloadStatus?> = MutableStateFlow(null)
    val downloading: StateFlow<DownloadStatus?> = _downloading.asStateFlow()


    fun fetchData() {
        fetchDataJob?.cancel()
        fetchDataJob = viewModelScope.launch {
            dataRepository.fetchData()
                .collect {
                    if (it != null)
                        _data.value = it
                }
        }
    }

    fun downloadFile2WithFlow(file: File, fileModel: FileModel) {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.downloadFile(fileModel.url).collect {
                when (it.status) {
                    Status.SUCCESS -> it.data?.let { it1 ->
                        writeResponseBodyToDisk(file, it1).collect {
                            withContext(Dispatchers.Main) {
                                it.file = fileModel
                                _downloading.emit(it)
                            }
                        }
                    }
                    Status.ERROR -> withContext(Dispatchers.Main) {
                        val status = DownloadStatus.Error("File not downloaded")
                        status.file = fileModel
                        _downloading.emit(status)
                    }
                }

            }
        }
    }

}