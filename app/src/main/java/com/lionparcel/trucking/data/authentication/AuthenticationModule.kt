package com.lionparcel.trucking.data.authentication

import com.lionparcel.trucking.data.authentication.mapper.AccessTokenMapper
import com.lionparcel.trucking.data.authentication.repository.AuthenticationDataRepository
import com.lionparcel.trucking.data.common.module.NetworkModule
import com.lionparcel.trucking.data.preference.Preference
import com.lionparcel.trucking.data.preference.module.PreferenceModule
import com.lionparcel.trucking.domain.authentication.AuthenticationRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, PreferenceModule::class])
class AuthenticationModule {

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): AuthenticationApi {
        return retrofit.create(AuthenticationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(
        authenticationApi: AuthenticationApi,
        preference: Preference,
        accessTokenMapper: AccessTokenMapper,
    ): AuthenticationRepository {
        return AuthenticationDataRepository(
            authenticationApi,
            preference,
            accessTokenMapper,
        )
    }
}
