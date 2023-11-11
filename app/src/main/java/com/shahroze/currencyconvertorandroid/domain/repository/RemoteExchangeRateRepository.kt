package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto

interface RemoteExchangeRateRepository {
    suspend fun getExchangeRate(): ExchangeRateResponseDto
    suspend fun getSymbols(): ExchangeRateResponseDto
}