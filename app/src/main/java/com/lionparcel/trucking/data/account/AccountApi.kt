package com.lionparcel.trucking.data.account

import com.lionparcel.trucking.data.account.entity.CheckForceLoginResponse
import io.reactivex.Single
import retrofit2.http.GET

interface AccountApi {

    //TODO DELETE SOON
    @GET("account/app/force_login/check")
    fun checkForceLogin(): Single<CheckForceLoginResponse>
}
