package com.najwa.task

import android.content.Context
import java.io.IOException
import java.io.InputStream

class Utils {
    companion object {
        fun getJsonFromAssets(context: Context, fileName: String): String {
            val jsonString: String = try {
                val inputStream: InputStream = context.assets.open(fileName)
                val size: Int = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }
            return jsonString
        }
    }
}