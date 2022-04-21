package com.najwa.task.data.api

import com.najwa.task.BuildConfig
import com.najwa.task.Utils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import javax.inject.Inject

private const val SUCCESS_CODE = 200

class MockInterceptor @Inject constructor(
    private val fakeData: String
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (BuildConfig.DEBUG) {
            val uri = chain.request().url.toUri().toString()
            val responseString = fakeData

            return chain.proceed(chain.request())
                .newBuilder()
                .code(SUCCESS_CODE)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    ResponseBody.create(
                        "application/json".toMediaTypeOrNull(),
                        responseString
                    )
                )
                .addHeader("content-type", "application/json")
                .build()
        } else {
            //just to be on safe side.
            throw IllegalAccessError(
                "MockInterceptor is only meant for Testing Purposes and " +
                        "bound to be used only with DEBUG mode"
            )
        }
    }
}
