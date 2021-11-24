package com.lionparcel.trucking.view.common.base

import androidx.lifecycle.ViewModelProvider
import com.lionparcel.trucking.view.common.module.DaggerViewModelFactoryComponent

abstract class BaseViewModelFactory : ViewModelProvider.Factory {

    protected val component by lazy { DaggerViewModelFactoryComponent.builder().build() }
}
