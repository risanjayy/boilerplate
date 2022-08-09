package com.lionparcel.trucking.data.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import com.lionparcel.trucking.data.common.utils.CryptoHelper
import com.lionparcel.trucking.domain.authentication.entity.AccessToken
import com.lionparcel.trucking.view.common.utils.DateUtils
import java.util.*

class PreferenceImpl(
    private val pref: SharedPreferences,
    private val cryptoHelper: CryptoHelper
) : Preference {

    companion object {
        private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
        private const val KEY_ACCESS_TOKEN_EXPIRED_AT = "KEY_ACCESS_TOKEN_EXPIRED_AT"
        private const val KEY_ACCESS_TOKEN_TYPE = "KEY_ACCESS_TOKEN_TYPE"
    }

    override fun getAccessToken(): AccessToken? {
        val accessTokenValue =
            cryptoHelper.decrypt(KEY_ACCESS_TOKEN, pref.getString(KEY_ACCESS_TOKEN, null).orEmpty())
        val accessTokenExpiredAt = cryptoHelper.decrypt(
            KEY_ACCESS_TOKEN_EXPIRED_AT,
            pref.getString(KEY_ACCESS_TOKEN_EXPIRED_AT, null).orEmpty()
        )
        val accessTokenType = cryptoHelper.decrypt(
            KEY_ACCESS_TOKEN_TYPE,
            pref.getString(KEY_ACCESS_TOKEN_TYPE, null).orEmpty()
        )
        return accessTokenValue.isEmpty()
            .or(accessTokenExpiredAt.isEmpty())
            .or(accessTokenType.isEmpty())
            .takeUnless { it }?.run {
                AccessToken(
                    accessTokenValue,
                    DateUtils.stringToDate(accessTokenExpiredAt, DateUtils.TIME_STAMP_FORMAT),
                    accessTokenType
                )
            }
    }

    override fun putAccessToken(accessToken: AccessToken) {
        pref.edit {
            putString(KEY_ACCESS_TOKEN, cryptoHelper.encrypt(KEY_ACCESS_TOKEN, accessToken.value))
            val encryptedExpiredAt = cryptoHelper.encrypt(
                KEY_ACCESS_TOKEN_EXPIRED_AT,
                DateUtils.dateToString(
                    accessToken.expiredAt,
                    DateUtils.TIME_STAMP_FORMAT
                )
            )
            putString(KEY_ACCESS_TOKEN_EXPIRED_AT, encryptedExpiredAt)
            putString(
                KEY_ACCESS_TOKEN_TYPE,
                cryptoHelper.encrypt(KEY_ACCESS_TOKEN_TYPE, accessToken.type)
            )
        }
    }

    override fun removeSession() {
        pref.edit {
            remove(KEY_ACCESS_TOKEN)
            remove(KEY_ACCESS_TOKEN_EXPIRED_AT)
            remove(KEY_ACCESS_TOKEN_TYPE)
        }
    }
}
