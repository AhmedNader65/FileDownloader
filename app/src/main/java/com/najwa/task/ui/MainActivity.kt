package com.najwa.task.ui

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.najwa.task.adapter.FilesAdapter
import com.najwa.task.databinding.ActivityMainBinding
import com.najwa.task.extension
import com.najwa.task.model.DownloadStatus
import com.najwa.task.model.FileModel
import com.najwa.task.model.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), FilesAdapter.OnFileInteract {
    private lateinit var adapter: FilesAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var basePath: String
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        basePath =
            getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + File.separator

        viewModel.fetchData()
        binding.filesList.itemAnimator = null
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { it ->
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.loading.visibility = GONE

                            it.data?.let { files ->
                                adapter = FilesAdapter(basePath, files, this@MainActivity)
                                binding.filesList.adapter = adapter
                            }
                        }
                        Status.LOADING -> {
                            binding.loading.visibility = VISIBLE
                        }
                        Status.ERROR -> {

                            binding.loading.visibility = GONE
                            it.message?.let { message ->

                                Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.downloading.collect { downloadStatus ->
                    when (downloadStatus) {
                        is DownloadStatus.Success -> {
                            Log.d("Downloading Completed", " done ")

                            downloadStatus.file?.let { fileModel ->
                                adapter.setDownloaded(
                                    fileModel,
                                    true
                                )
                            }
                        }
                        is DownloadStatus.Error -> {
                            Log.d("Downloading Error", "error ")

                        }
                        is DownloadStatus.Progress -> {
                            downloadStatus.file?.let { fileModel ->
                                adapter.setDownloading(
                                    fileModel,
                                    true
                                )
                            }
                            downloadStatus.file?.let { fileModel ->
                                adapter.setProgress(
                                    fileModel,
                                    downloadStatus.length,
                                    downloadStatus.downloaded
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDownloadFile(fileModel: FileModel) {
        viewModel.downloadFile2WithFlow(
            File(basePath + fileModel.name + "." + fileModel.url.extension),
            fileModel
        )
    }

    override fun onOpenPdfFile(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(FileProvider.getUriForFile(applicationContext,
            "$packageName.provider", file), "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }

    override fun onOpenVideoFile(file: File) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(FileProvider.getUriForFile(applicationContext,
            "$packageName.provider", file), "video/mp4")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }



}