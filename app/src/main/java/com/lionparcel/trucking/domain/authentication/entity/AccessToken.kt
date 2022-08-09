package com.lionparcel.trucking.domain.authentication.entity

import java.util.*

data class AccessToken(val value: String, val expiredAt: Date, val type: String)  {

    fun hasExpired(): Boolean = expiredAt.before(Date())
}
