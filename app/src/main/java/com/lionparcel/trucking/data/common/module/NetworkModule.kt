package com.lionparcel.trucking.data.common.module

import com.lionparcel.trucking.data.preference.module.PreferenceModule
import com.lionparcel.trucking.domain.common.module.CommonModule
import dagger.Module

@Module(includes = [CommonModule::class, PreferenceModule::class])
class NetworkModule {

}
