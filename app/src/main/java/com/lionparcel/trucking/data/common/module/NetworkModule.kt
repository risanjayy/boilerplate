package com.lionparcel.trucking.data.common.module

import com.lionparcel.trucking.BuildConfig
import com.lionparcel.trucking.data.common.interceptor.NetworkAvailabilityInterceptor
import com.lionparcel.trucking.data.preference.module.PreferenceModule
import com.lionparcel.trucking.domain.common.module.CommonModule
import com.lionparcel.trucking.view.app.App
import com.readystatesoftware.chuck.ChuckInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module(includes = [CommonModule::class, PreferenceModule::class])
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitLionParcel(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        rxJava2CallAdapterFactory: RxJava2CallAdapterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(gsonConverterFactory)
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(rxJava2CallAdapterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Singleton
    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory {
        return RxJava2CallAdapterFactory.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        interceptors: ArrayList<Interceptor>
    ): OkHttpClient {
        val clientBuilder = OkHttpClient.Builder()
        interceptors.forEach { clientBuilder.addInterceptor(it) }
        clientBuilder.connectTimeout(30, TimeUnit.SECONDS)
        clientBuilder.readTimeout(30, TimeUnit.SECONDS)
        clientBuilder.writeTimeout(30, TimeUnit.SECONDS)
        return clientBuilder.build()
    }

    @Singleton
    @Provides
    fun provideInterceptors(): ArrayList<Interceptor> {
        val interceptors = arrayListOf<Interceptor>()

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
        val noCacheInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Cache-Control", "max-age=0")
                .build()
            chain.proceed(request)
        }
        interceptors.add(NetworkAvailabilityInterceptor(App.instance))
        interceptors.add(ChuckInterceptor(App.instance))
        interceptors.add(noCacheInterceptor)
        interceptors.add(loggingInterceptor)

        return interceptors
    }

}
