package com.lionparcel.trucking.domain.account

import io.reactivex.Single

interface AccountRepository {

    fun getForceLogin(): Single<Boolean>
}
