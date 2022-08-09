package com.lionparcel.trucking.view.common.module

import com.lionparcel.trucking.view.login.LoginViewModelFactory
import com.lionparcel.trucking.view.main.MainViewModelFactory
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FeatureModule::class])
interface ViewModelFactoryComponent {
    fun inject(target: MainViewModelFactory)
    fun inject(target: LoginViewModelFactory)
}
