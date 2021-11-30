package com.lionparcel.trucking.view.common.extensions

import android.util.Log
import io.reactivex.Completable
import io.reactivex.disposables.Disposable

fun Completable.safeSubscribe(success: () -> Unit): Disposable {
    return this.subscribe(success, { Log.d(this::class.java.simpleName, it.localizedMessage, it) })
}

fun Completable.dispose(): Disposable {
    return safeSubscribe { }
}
