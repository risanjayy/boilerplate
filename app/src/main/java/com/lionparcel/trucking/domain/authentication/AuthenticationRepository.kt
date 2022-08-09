package com.lionparcel.trucking.domain.authentication

import io.reactivex.Completable

interface AuthenticationRepository {

    fun login(): Completable
}
