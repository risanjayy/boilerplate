package com.lionparcel.trucking.view.app

import androidx.multidex.MultiDexApplication

class App: MultiDexApplication() {

    companion object {
        lateinit var instance: App
            private set
    }
}
