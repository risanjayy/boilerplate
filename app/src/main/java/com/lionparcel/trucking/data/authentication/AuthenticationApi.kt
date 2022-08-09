package com.lionparcel.trucking.data.authentication

import com.lionparcel.trucking.data.authentication.entity.AccessTokenResponse
import io.reactivex.Single
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("url-login-api")
    fun login(): Single<AccessTokenResponse>
}
