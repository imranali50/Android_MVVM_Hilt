package com.app.mvvm

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import com.app.mvvm.util.ProgressLoading
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()

    }
    companion object {
        val instance: MyApplication by lazy { getMainInstance() }
        val progressBar: ProgressLoading = ProgressLoading()

        private fun getMainInstance(): MyApplication {
            return MyApplication()
        }

    }
}