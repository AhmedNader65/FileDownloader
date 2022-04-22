package com.najwa.task

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.najwa.task.model.DownloadStatus
import com.najwa.task.model.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.*


fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

suspend fun <T> getResponse(
    request: suspend () -> Response<T>,
    defaultErrorMessage: String
): Resource<T> {
    return try {
        val result = request.invoke()
        if (result.isSuccessful) {
            return Resource.success(result.body())
        } else {
            Resource.error(result.message(), null)
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        Resource.error("Unknown Error", null)
    }
}

val String.extension: String
    get() = substringAfterLast('.', "")

fun writeResponseBodyToDisk(file: File, body: ResponseBody): Flow<DownloadStatus> {
    return flow {
        try {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(file)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    emit(
                        DownloadStatus.Progress(
                            fileSize,
                            fileSizeDownloaded
                        )
                    )
                    Log.d(
                        "MainActivityViewModel",
                        "file download: $fileSizeDownloaded of $fileSize"
                    )
                }
                outputStream.flush()
                emit(
                    DownloadStatus.Success
                )
            } catch (e: IOException) {
                emit(DownloadStatus.Error("File not downloaded"))
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            emit(DownloadStatus.Error("File not downloaded"))
        }
    }
}

fun bytesToHumanReadableSize(bytes: Float) = when {
    bytes >= 1 shl 30 -> "%.1f GB".format(bytes / (1 shl 30))
    bytes >= 1 shl 20 -> "%.1f MB".format(bytes / (1 shl 20))
    bytes >= 1 shl 10 -> "%.0f kB".format(bytes / (1 shl 10))
    else -> "$bytes bytes"
}