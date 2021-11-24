package com.lionparcel.trucking.data.account

import com.lionparcel.trucking.data.account.repository.AccountDataRepository
import com.lionparcel.trucking.data.common.module.NetworkModule
import com.lionparcel.trucking.domain.account.AccountRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
class AccountModule {

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): AccountApi {
        return retrofit.create(AccountApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(
        accountApi: AccountApi
    ): AccountRepository {
        return AccountDataRepository(accountApi)
    }
}
