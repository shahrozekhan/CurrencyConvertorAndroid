package com.shahroze.currencyconvertorandroid.di.modules

import com.shahroze.currencyconvertorandroid.BuildConfig
import com.shahroze.currencyconvertorandroid.data.remote.getway.ExchangeRateGateWay
import com.shahroze.currencyconvertorandroid.di.Interceptor.ExchangeRateApiInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetWorkModule {
    @Provides
    @ExChangeRateApi
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideInterceptor(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG)
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient().newBuilder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(ExchangeRateApiInterceptor())
            .build()
    }

    @Provides

    fun provideExchangeRateService(@ExChangeRateApi retrofit: Retrofit): ExchangeRateGateWay {
        return retrofit.create(ExchangeRateGateWay::class.java)
    }


}