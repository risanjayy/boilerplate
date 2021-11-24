package com.lionparcel.trucking.data.account.repository

import com.lionparcel.trucking.data.account.AccountApi
import com.lionparcel.trucking.data.common.exceptions.MissingFieldsException
import com.lionparcel.trucking.domain.account.AccountRepository
import io.reactivex.Single
import javax.inject.Inject

class AccountDataRepository @Inject constructor(
    private val accountApi: AccountApi
): AccountRepository {

    //TODO DELETE SOON
    override fun getForceLogin(): Single<Boolean> {
        return accountApi.checkForceLogin().map {
            it.isForceLogin ?: throw MissingFieldsException(listOf("isForceLogin"))
        }
    }
}
