package com.najwa.task.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.najwa.task.R
import com.najwa.task.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val x =Utils.getJsonFromAssets(this,"getListOfFilesResponse.json")
        Log.e("x",x!!)
    }
}