package com.najwa.task.model

sealed class DownloadStatus {
    var file:FileModel?=null
    object Success : DownloadStatus()

    data class Error(val message: String) : DownloadStatus()

    data class Progress(val length: Long,val downloaded:Long): DownloadStatus()

}