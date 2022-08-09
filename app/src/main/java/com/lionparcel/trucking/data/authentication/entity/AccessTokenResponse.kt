package com.lionparcel.trucking.data.authentication.entity

import com.google.gson.annotations.SerializedName
import java.util.*

data class AccessTokenResponse(
    @SerializedName("token") val accessToken: String?,
    @SerializedName("expired_at") val expiredAt: Date?,
    @SerializedName("type") val type: String?
)
