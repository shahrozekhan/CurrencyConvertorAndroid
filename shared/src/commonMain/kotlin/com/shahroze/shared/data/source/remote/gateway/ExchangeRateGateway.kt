package com.shahroze.shared.data.source.remote.gateway

import com.shahroze.shared.data.dto.ExchangeRateResponseDto
import com.shahroze.shared.data.source.remote.helpers.KtorClientConstants
import io.ktor.client.call.*
import io.ktor.client.request.*

class ExchangeRateGateway {
    private object ExchangeRateRoutes {
        const val GET_EXCHANGERATE = "/v1/latest"
        const val GET_SYMBOLS = "/v1/symbols"
    }

    suspend fun getExchangeRate(): ExchangeRateResponseDto {
        return KtorClientConstants.client.get { url(KtorClientConstants.baseUrl + ExchangeRateRoutes.GET_EXCHANGERATE) }
            .body()
    }

    suspend fun getSymbols(): ExchangeRateResponseDto {
        return KtorClientConstants.client.get { url(KtorClientConstants.baseUrl + ExchangeRateRoutes.GET_SYMBOLS) }
            .body()
    }

}