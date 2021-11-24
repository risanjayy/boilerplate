package com.lionparcel.trucking.domain.common

import io.reactivex.Completable
import io.reactivex.CompletableSource
import io.reactivex.CompletableTransformer
import javax.inject.Inject

class CompletableErrorTransformer @Inject constructor(private val errorHelper: ErrorHelper) : CompletableTransformer {

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.onErrorResumeNext {
            Completable.error(errorHelper.buildThrowable(it))
        }
    }
}
