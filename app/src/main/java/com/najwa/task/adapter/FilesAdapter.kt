package com.najwa.task.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.najwa.task.R
import com.najwa.task.bytesToHumanReadableSize
import com.najwa.task.databinding.FileItemBinding
import com.najwa.task.model.FileModel

class FilesAdapter(
    private var mDataList: ArrayList<FileModel>,
    private val listener: OnFileInteract
) :
    RecyclerView.Adapter<FilesAdapter.ViewHolder>() {

    private lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val itemBinding =
            FileItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val file = mDataList[position]

        holder.mBinding.fileName.text = file.name
        holder.mBinding.fileTypeIcon.setImageResource(getTypeIcon(file.type))
        holder.mBinding.fileDownloadIcon.setOnClickListener {
            listener.onDownloadFile(file)

        }
        holder.mBinding.progressbar.isVisible = file.isDownloading
        holder.mBinding.downloadedSize.isVisible = file.isDownloading
        holder.mBinding.fileDownloadIcon.isVisible = !file.isDownloading
        if (file.length <= 0) {
            holder.mBinding.downloadedSize.text =
                bytesToHumanReadableSize(file.downloadedSize.toFloat())
            holder.mBinding.progressbar.isIndeterminate = true
        } else {
            holder.mBinding.downloadedSize.text = context.getString(
                R.string.downloaded,
                bytesToHumanReadableSize(file.downloadedSize.toFloat()),
                bytesToHumanReadableSize(
                    file.length.toFloat()
                )
            )
            holder.mBinding.progressbar.isIndeterminate = false
            holder.mBinding.progressbar.progress = file.progress()
        }
    }


    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun setDownloading(file: FileModel, isDownloading: Boolean) {
        getFile(file)?.isDownloading = isDownloading
        notifyItemChanged(mDataList.indexOf(file))
    }

    fun setProgress(file: FileModel, length: Long, downloadedSize: Long) {
        val mFile = getFile(file)
        mFile?.length = length
        mFile?.downloadedSize = downloadedSize
        notifyItemChanged(mDataList.indexOf(file))
    }

    private fun getFile(file: FileModel) =
        mDataList.find { file.id == it.id }


    class ViewHolder(binding: FileItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val mBinding = binding
    }

    private fun getTypeIcon(type: String): Int {
        return when (type) {
            "VIDEO" -> R.drawable.ic_vid_file
            else -> R.drawable.ic_pdf_file
        }
    }

    interface OnFileInteract {
        fun onDownloadFile(fileModel: FileModel)
    }
}