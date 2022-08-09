package com.lionparcel.trucking.domain.authentication.usecase

import com.lionparcel.trucking.domain.authentication.AuthenticationRepository
import com.lionparcel.trucking.domain.common.CompletableErrorTransformer
import com.lionparcel.trucking.domain.common.base.BaseCompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    errorTransformer: CompletableErrorTransformer
) : BaseCompletableUseCase(errorTransformer) {

    override fun buildSingle(data: Map<String, Any?>): Completable {
        return authenticationRepository.login()
    }
}
