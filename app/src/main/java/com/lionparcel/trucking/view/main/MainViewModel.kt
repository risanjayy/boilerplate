package com.lionparcel.trucking.view.main

import android.util.Log
import com.lionparcel.trucking.domain.account.usecase.GetForceLoginUseCase
import com.lionparcel.trucking.view.common.base.BaseViewModel

class MainViewModel(
    private val getForceLoginUseCase: GetForceLoginUseCase
): BaseViewModel() {

    override fun initPage() {
        super.initPage()
        requestForceLogin()
    }

    //TODO DELETE SOON
    private fun requestForceLogin() {
        getForceLoginUseCase.execute().asPageLoadEventSource().subscribe({
            Log.i("MASOEK", it.toString())
        }, {
            Log.i("ERROR", it.localizedMessage ?: "TEST")
        }).collect()
    }
}
