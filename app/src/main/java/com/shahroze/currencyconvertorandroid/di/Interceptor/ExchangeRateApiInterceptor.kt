package com.shahroze.currencyconvertorandroid.di.Interceptor

import com.shahroze.currencyconvertorandroid.BuildConfig.EXCHANGE_RATE_API_KEY
import com.shahroze.currencyconvertorandroid.common.Constants.ExhangeRateParams.ACCESS_KEY
import com.shahroze.currencyconvertorandroid.common.enums.HttpMethodType
import okhttp3.Interceptor
import okhttp3.Response

class ExchangeRateApiInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalAPIRequest = chain.request()
        val newRequest = originalAPIRequest.newBuilder()
        when (originalAPIRequest.method) {
            HttpMethodType.GET.type -> {
                val apiKeyURL = originalAPIRequest.url.newBuilder()
                    .addQueryParameter(
                        ACCESS_KEY,
                        EXCHANGE_RATE_API_KEY
                    )
                    .build()
                newRequest.url(apiKeyURL)
            }
        }
        return chain.proceed(newRequest.build())
    }
}