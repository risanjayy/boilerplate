package com.lionparcel.trucking.view.main

import androidx.lifecycle.ViewModel
import com.lionparcel.trucking.domain.account.usecase.GetForceLoginUseCase
import com.lionparcel.trucking.view.common.base.BaseViewModelFactory
import javax.inject.Inject

class MainViewModelFactory : BaseViewModelFactory() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var getForceLoginUseCase: GetForceLoginUseCase

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(MainViewModel(getForceLoginUseCase)) as T
    }
}
