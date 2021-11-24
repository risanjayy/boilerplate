package com.lionparcel.trucking.view.app

import android.util.Log
import androidx.multidex.MultiDexApplication
import io.reactivex.plugins.RxJavaPlugins

class App: MultiDexApplication() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        RxJavaPlugins.setErrorHandler {
            Log.d("Rx Unhandled Error", it.localizedMessage ?: it.message ?: String())
        }
    }
}
