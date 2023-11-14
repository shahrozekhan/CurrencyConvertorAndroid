package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import kotlinx.coroutines.flow.Flow

interface ExchangeRateRepository {
    fun getExchangeRateFromRemote(): Flow<RemoteResource<List<ExchangeRateDto>>>

    suspend fun insertExchangeRatesToDatabase(listOfExchangeRate: List<ExchangeRateDto>)

    suspend fun getExchangeRatesFromDatabase(): List<ExchangeRateDto>

    fun getExchangeRatesFromAssets(): Flow<Resource<List<ExchangeRateDto>>>

}