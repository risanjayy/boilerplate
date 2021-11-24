package com.lionparcel.trucking.data.common.module

import com.lionparcel.trucking.data.common.utils.CryptoHelper
import com.lionparcel.trucking.view.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EncryptionModule {

    @Singleton
    @Provides
    fun provideCryptoHelper(): CryptoHelper {
        return CryptoHelper(App.instance.applicationContext)
    }
}
