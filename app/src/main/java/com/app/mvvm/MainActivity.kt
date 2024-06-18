package com.app.mvvm

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.app.mvvm.databinding.ActivityMainBinding
import com.app.mvvm.model.viewModel.MainViewModel
import com.app.mvvm.util.NetworkResult
import com.app.mvvm.util.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val b: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel by viewModels<MainViewModel>()
    lateinit var pref: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(b.root)
        pref = PreferenceManager(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fetchData()
    }

    private fun fetchData() {
        mainViewModel.fetchDogResponse()

        mainViewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    MyApplication.progressBar.dismiss()
                    Log.e("TAG", "fetchData: " + response.data?.status)
                    Log.e("TAG", "fetchData: " + response.data?.message)
                }

                is NetworkResult.Error -> {
                    // Handle the error case
                    MyApplication.progressBar.dismiss()
                }

                is NetworkResult.Loading -> {
                    // Handle the loading case
                    MyApplication.progressBar.show(this)
                }

                else -> {
                    // Handle other cases
                    MyApplication.progressBar.dismiss()
                }
            }
        }
    }
}
