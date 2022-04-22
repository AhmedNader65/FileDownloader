package com.najwa.task.model

data class FileModel(val id: Int, val type: String, val url: String, val name: String) {
    fun progress(): Int =
        (downloadedSize * 100 / length).toInt()

    var isDownloading: Boolean = false
    var isCompleted: Boolean = false
    var length: Long = 0
    var downloadedSize: Long = 0
}