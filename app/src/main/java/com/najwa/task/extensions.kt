package com.najwa.task

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.najwa.task.adapter.BaseViewHolder
import com.najwa.task.adapter.SimpleRecyclerAdapter
import com.najwa.task.model.Resource
import retrofit2.Response


fun ViewGroup.inflater(layoutRes: Int): View =
    LayoutInflater.from(context).inflate(layoutRes, this, false)

fun <T : Any> RecyclerView.withSimpleAdapter(
    dataList: List<T>, @LayoutRes layoutID: Int,
    onBindView: BaseViewHolder<T>.(data: T) -> Unit
): SimpleRecyclerAdapter<T> {
    val recyclerAdapter = SimpleRecyclerAdapter(dataList, layoutID, onBindView)
    adapter = recyclerAdapter
    return recyclerAdapter
}

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

