package com.lionparcel.trucking.view.common.extensions

import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.safeSubscribe(success: (T) -> Unit): Disposable {
    return this.subscribe(success, { Log.d(this::class.java.simpleName, it.localizedMessage, it) })
}

fun <T> Observable<T>.dispose(): Disposable {
    return safeSubscribe { }
}
