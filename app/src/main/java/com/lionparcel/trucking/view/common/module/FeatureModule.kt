package com.lionparcel.trucking.view.common.module

import com.lionparcel.trucking.data.account.AccountModule
import com.lionparcel.trucking.data.authentication.AuthenticationModule
import dagger.Module

@Module(
    includes = [
        AccountModule::class,
        AuthenticationModule::class
    ]
)
class FeatureModule
