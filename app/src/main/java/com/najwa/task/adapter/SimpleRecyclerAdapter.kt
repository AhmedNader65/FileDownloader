package com.najwa.task.adapter

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.najwa.task.model.FileModel

open class SimpleRecyclerAdapter(
    data: ArrayList<FileModel>, @LayoutRes layoutID: Int,
    private val onBindView: BaseViewHolder<FileModel>.(data: FileModel) -> Unit
) : BaseRecyclerAdapter<FileModel>(data) {

    override val layoutItemId: Int = layoutID

    override fun onBindViewHolder(holder: BaseViewHolder<FileModel>, position: Int) {
        holder.onBindView(dataList[position])
    }
}