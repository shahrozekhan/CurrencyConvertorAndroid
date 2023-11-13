package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import kotlinx.coroutines.flow.Flow

interface RemoteExchangeRateRepository {
    fun getExchangeRate(): Flow<RemoteResource<ExchangeRateResponseDto>>

    fun getSymbols(): Flow<RemoteResource<ExchangeRateResponseDto>>
}