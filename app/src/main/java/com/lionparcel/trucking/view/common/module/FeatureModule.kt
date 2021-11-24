package com.lionparcel.trucking.view.common.module

import com.lionparcel.trucking.data.account.AccountModule
import dagger.Module

@Module(
    includes = [
        AccountModule::class
    ]
)
class FeatureModule
