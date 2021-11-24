package com.lionparcel.trucking.domain.account.usecase

import com.lionparcel.trucking.domain.account.AccountRepository
import com.lionparcel.trucking.domain.common.SingleErrorTransformer
import com.lionparcel.trucking.domain.common.base.BaseSingleUseCase
import io.reactivex.Single
import javax.inject.Inject

//TODO DELETE SOON
class GetForceLoginUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    errorTransformer: SingleErrorTransformer<Boolean>
): BaseSingleUseCase<Boolean>(errorTransformer) {

    override fun buildSingle(data: Map<String, Any?>): Single<Boolean> {
        return accountRepository.getForceLogin()
    }
}
