package com.shahroze.currencyconvertorandroid.data.remote.getway

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import retrofit2.http.GET

interface ExchangeRateGateWay {

    @GET("/v1/latest")
    suspend fun getExchangeRate(): ExchangeRateResponseDto

    @GET("/v1/symbols")
    suspend fun getSymbols(): ExchangeRateResponseDto
}