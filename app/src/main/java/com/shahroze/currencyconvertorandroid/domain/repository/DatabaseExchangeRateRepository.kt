package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto

interface DatabaseExchangeRateRepository {

    suspend fun insertExchangeRates(listOfExchangeRate: List<ExchangeRateDto>)

    suspend fun getExchangeRates(): List<ExchangeRateDto>

    suspend fun getFavoriteExchangeRates(): List<ExchangeRateDto>

    suspend fun markExchangeRateFavorite(exchangeRateDto: ExchangeRateDto)


}