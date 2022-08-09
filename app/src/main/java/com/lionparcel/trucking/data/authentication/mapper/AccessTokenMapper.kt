package com.lionparcel.trucking.data.authentication.mapper

import com.lionparcel.trucking.data.authentication.entity.AccessTokenResponse
import com.lionparcel.trucking.data.common.base.BaseMapper
import com.lionparcel.trucking.data.common.exceptions.MissingFieldsException
import com.lionparcel.trucking.domain.authentication.entity.AccessToken
import javax.inject.Inject

class AccessTokenMapper @Inject constructor() : BaseMapper<AccessTokenResponse, AccessToken>() {
    override fun map(item: AccessTokenResponse): AccessToken {
        return item.run {
            val missingFields = mutableListOf<String>()
            accessToken ?: missingFields.add("accessToken")
            expiredAt ?: missingFields.add("expiredAt")
            type ?: missingFields.add("type")
            if (missingFields.isNotEmpty()) throw MissingFieldsException(missingFields)
            AccessToken(
                accessToken!!,
                expiredAt!!,
                type!!
            )
        }
    }
}
