package com.lionparcel.trucking.view.common

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

open class ActionLiveData<T> : MutableLiveData<T> {

    constructor() : super()
    constructor(data: T) : super(data)

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { data ->
            if (data == null) return@Observer
            observer.onChanged(data)
            value = null
        })
    }

    @MainThread
    open fun sendAction(data: T) {
        value = data
        value = null
    }
}
