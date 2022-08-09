package com.lionparcel.trucking.data.preference

import com.lionparcel.trucking.domain.authentication.entity.AccessToken

interface Preference {

    fun putAccessToken(accessToken: AccessToken)

    fun getAccessToken(): AccessToken?

    fun removeSession()
}
