package com.lionparcel.trucking.data.common.interceptor

import android.content.Context
import com.lionparcel.trucking.data.common.exceptions.NoNetworkException
import com.lionparcel.trucking.view.common.extensions.isOnline
import okhttp3.Interceptor
import okhttp3.Response

class NetworkAvailabilityInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!context.isOnline()) {
            throw NoNetworkException(context)
        } else {
            chain.proceed(chain.request())
        }
    }
}
