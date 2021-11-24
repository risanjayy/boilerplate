package com.lionparcel.trucking.data.preference.module

import android.content.Context
import android.content.SharedPreferences
import com.lionparcel.trucking.data.preference.Preference
import com.lionparcel.services.consumer.data.preference.PreferenceImpl
import com.lionparcel.trucking.data.preference.PreferenceType.LION_PARCEL
import com.lionparcel.trucking.BuildConfig
import com.lionparcel.trucking.data.common.module.EncryptionModule
import com.lionparcel.trucking.data.common.utils.CryptoHelper
import com.lionparcel.trucking.view.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [EncryptionModule::class])
class PreferenceModule {

    @Singleton
    @Provides
    @Named(LION_PARCEL)
    fun provideSharedPref(): SharedPreferences {
        return App.instance.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providePreference(
        @Named(LION_PARCEL) pref: SharedPreferences,
        cryptoHelper: CryptoHelper
    ): Preference {
        return PreferenceImpl(pref, cryptoHelper)
    }
}
