package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    fun getFlowExchangeRateFromRemote(): Flow<RemoteResource<List<ExchangeRateDto>>>

    suspend fun getExchangeRateFromRemote(): RemoteResource<List<ExchangeRateDto>>

    suspend fun insertExchangeRatesToDatabase(listOfExchangeRate: List<ExchangeRateDto>)

    suspend fun getExchangeRatesFromDatabase(): List<ExchangeRateDto>

    fun getFlowExchangeRatesFromAssets(): Flow<Resource<List<ExchangeRateDto>>>

    suspend fun getExchangeRatesFromAssets(): Resource<List<ExchangeRateDto>>
}