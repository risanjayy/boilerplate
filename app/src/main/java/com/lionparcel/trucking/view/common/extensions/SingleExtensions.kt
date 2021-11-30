package com.lionparcel.trucking.view.common.extensions

import android.os.Build
import android.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Single<T>.safeSubscribe(success: (T) -> Unit): Disposable {
    return this.subscribe(success, { Log.d(this::class.java.simpleName, it.localizedMessage, it) })
}

fun <T> Single<T>.dispose(): Disposable {
    return safeSubscribe { }
}

fun <T> Single<T>.delaySupportSDK(
    time: Long,
    unit: TimeUnit,
    delayError: Boolean,
    supportSDKVersion: Int
): Single<T> {
    return (if (Build.VERSION.SDK_INT <= supportSDKVersion) {
        this.delay(time, unit, delayError)
    } else this)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
