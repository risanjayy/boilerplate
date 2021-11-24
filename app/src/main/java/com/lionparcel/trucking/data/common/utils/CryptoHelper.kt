package com.lionparcel.trucking.data.common.utils

import android.content.Context
import com.yakivmospan.scytale.Crypto
import com.yakivmospan.scytale.Options
import com.yakivmospan.scytale.Store

class CryptoHelper(context: Context) {

    private val store = Store(context)

    private val crypto = Crypto(Options.TRANSFORMATION_SYMMETRIC)

    fun registerKey(key: String) {
        if (!store.hasKey(key)) {
            store.generateSymmetricKey(key, null)
        }
    }

    fun decrypt(key: String, data: String): String {
        val secretKey = store.getSymmetricKey(key, null)
        return data.takeIf { it.isNotEmpty() && it.split(IV_SEPARATOR).size > 1 }
            ?.let { crypto.decrypt(data, secretKey) } ?: ""
    }

    fun encrypt(key: String, value: String): String {
        val secretKey = store.getSymmetricKey(key, null)
        return crypto.encrypt(value, secretKey)
    }

    companion object {
        const val IV_SEPARATOR = "]"
    }
}
