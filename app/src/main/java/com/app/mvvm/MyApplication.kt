package com.app.mvvm

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication :Application() {

    override fun onCreate() {
        super.onCreate()

    }
}