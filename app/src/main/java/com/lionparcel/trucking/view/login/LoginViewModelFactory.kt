package com.lionparcel.trucking.view.login

import androidx.lifecycle.ViewModel
import com.lionparcel.trucking.domain.authentication.usecase.LoginUseCase
import com.lionparcel.trucking.view.common.base.BaseViewModelFactory
import javax.inject.Inject

class LoginViewModelFactory: BaseViewModelFactory() {

    init {
        component.inject(this)
    }

    @Inject
    lateinit var loginUseCase: LoginUseCase

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(LoginViewModel(loginUseCase)) as T
    }
}
