package com.lionparcel.trucking.data.authentication.repository

import com.lionparcel.trucking.data.authentication.AuthenticationApi
import com.lionparcel.trucking.data.authentication.entity.AccessTokenResponse
import com.lionparcel.trucking.data.authentication.mapper.AccessTokenMapper
import com.lionparcel.trucking.data.preference.Preference
import com.lionparcel.trucking.domain.authentication.AuthenticationRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AuthenticationDataRepository @Inject constructor(
    private val api: AuthenticationApi,
    private val preference: Preference,
    private val accessTokenMapper: AccessTokenMapper,
) : AuthenticationRepository {

    override fun login(): Completable {
        return api.login()
            .onErrorResumeNext {
                Single.just(AccessTokenResponse("oaskdoasdkosadk", Date(), "Barearere"))
            }
            .map(accessTokenMapper::apply)
            .map(preference::putAccessToken)
            .flatMapCompletable { Completable.complete() }
    }
}
