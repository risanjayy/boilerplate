package com.lionparcel.trucking.data.common.exceptions

import android.content.Context
import com.lionparcel.trucking.R
import java.io.IOException

class NoNetworkException(private val context: Context) : IOException() {

    override fun getLocalizedMessage(): String {
        return context.getString(R.string.general_no_internet_error_message)
    }
}
