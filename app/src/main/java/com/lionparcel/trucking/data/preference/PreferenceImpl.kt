package com.lionparcel.services.consumer.data.preference

import android.content.SharedPreferences
import com.lionparcel.trucking.data.common.utils.CryptoHelper
import com.lionparcel.trucking.data.preference.Preference

class PreferenceImpl(
    private val pref: SharedPreferences,
    private val cryptoHelper: CryptoHelper
) : Preference {

}
