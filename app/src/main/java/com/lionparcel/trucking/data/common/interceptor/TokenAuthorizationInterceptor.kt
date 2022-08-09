package com.lionparcel.trucking.data.common.interceptor

import com.lionparcel.trucking.data.preference.Preference
import okhttp3.Interceptor
import okhttp3.Response

class TokenAuthorizationInterceptor(
    private val preference: Preference
) : Interceptor {

    companion object {
        private const val REQUEST_TOKEN_KEY = "Authorization"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalBuilder = originalRequest.newBuilder()
        preference.getAccessToken()?.let {
            originalBuilder.addHeader(REQUEST_TOKEN_KEY, "${it.type} ${it.value}")
        }
        val initialResponse = chain.proceed(originalBuilder.build())
        if (initialResponse.code == 401) {
            preference.removeSession()
            throw Throwable()
        }
        return initialResponse
    }
}
