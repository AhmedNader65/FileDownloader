package com.najwa.task.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.najwa.task.data.repository.DataRepository
import com.najwa.task.model.FileModel
import com.najwa.task.model.Resource
import com.najwa.task.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {
    private var fetchDataJob: Job? = null

    private val _data = MutableStateFlow(Resource<ArrayList<FileModel>>(Status.INIT, null, null))
    val data: StateFlow<Resource<ArrayList<FileModel>>> = _data.asStateFlow()

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
}