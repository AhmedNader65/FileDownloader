package com.najwa.task.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.najwa.task.R
import com.najwa.task.databinding.ActivityMainBinding
import com.najwa.task.model.Status
import com.najwa.task.withSimpleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.fetchData()
        collectData()
    }

    private fun collectData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.data.collect { it ->
                    when (it.status) {
                        Status.SUCCESS -> {
                            binding.loading.visibility = GONE

                            it.data?.let { it1 ->
                                binding.filesList.withSimpleAdapter(it1, R.layout.file_item) {
                                    val fileName: TextView = itemView.findViewById(R.id.file_name)
                                    val fileTypeIcon: ImageView = itemView.findViewById(R.id.file_type_icon)
                                    fileName.text = it.name
                                    fileTypeIcon.setImageResource(getTypeIcon(it.type))
                                }
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
    }

    private fun getTypeIcon(type: String): Int {
        return when(type) {
            "VIDEO" -> R.drawable.ic_vid_file
            else -> R.drawable.ic_pdf_file
        }
    }
}