package com.shahroze.currencyconvertorandroid.data.remote.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import com.shahroze.currencyconvertorandroid.data.remote.getway.ExchangeRateGateWay
import com.shahroze.currencyconvertorandroid.domain.repository.RemoteExchangeRateRepository
import javax.inject.Inject

class RemoteExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateGateWay: ExchangeRateGateWay
) : RemoteExchangeRateRepository {
    override suspend fun getExchangeRate(): ExchangeRateResponseDto {
        return exchangeRateGateWay.getExchangeRate()
    }

    override suspend fun getSymbols(): ExchangeRateResponseDto {
        return exchangeRateGateWay.getSymbols()
    }
}