package com.lionparcel.trucking.view.login

import androidx.lifecycle.LiveData
import com.lionparcel.trucking.domain.authentication.usecase.LoginUseCase
import com.lionparcel.trucking.view.common.ActionLiveData
import com.lionparcel.trucking.view.common.Resource
import com.lionparcel.trucking.view.common.Status
import com.lionparcel.trucking.view.common.base.BaseViewModel

class LoginViewModel(
    private val loginUseCase: LoginUseCase
): BaseViewModel() {

    private val loginActionLiveData = ActionLiveData<Resource<Unit>>()
    internal val loginLiveData: LiveData<Resource<Unit>> get() = loginActionLiveData

    fun login() {
        loginActionLiveData.value = Resource(Status.LOADING)
        loginUseCase.execute().subscribe({
            loginActionLiveData.value = Resource(Status.SUCCESS)
        }, {
            loginActionLiveData.value = Resource(Status.ERROR)
        }).collect()
    }
}
