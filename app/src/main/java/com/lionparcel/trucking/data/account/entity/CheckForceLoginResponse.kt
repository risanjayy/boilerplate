package com.lionparcel.trucking.data.account.entity

import com.google.gson.annotations.SerializedName

data class CheckForceLoginResponse(
    @SerializedName("is_force_login") val isForceLogin: Boolean?
)
