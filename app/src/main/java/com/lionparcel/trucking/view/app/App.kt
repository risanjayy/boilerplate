package com.lionparcel.trucking.view.app

import androidx.multidex.MultiDexApplication

class App: MultiDexApplication() {

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
