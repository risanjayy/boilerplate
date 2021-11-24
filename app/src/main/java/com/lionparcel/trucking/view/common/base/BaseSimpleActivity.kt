package com.lionparcel.trucking.view.common.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding

abstract class BaseSimpleActivity<T : BaseViewModel, V : ViewBinding> : BaseActivity<V>() {

    protected val viewModel by lazy { buildViewModel() }

    protected abstract fun buildViewModel(): T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initLiveDataObservers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPage()
    }

    @CallSuper
    protected open fun initLiveDataObservers() = Unit
}
