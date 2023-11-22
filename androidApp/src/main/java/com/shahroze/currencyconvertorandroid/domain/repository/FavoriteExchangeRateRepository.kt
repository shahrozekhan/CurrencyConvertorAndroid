package com.shahroze.currencyconvertorandroid.domain.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto

interface FavoriteExchangeRateRepository {

    suspend fun getFavoriteExchangeRates(): List<ExchangeRateDto>

    suspend fun markExchangeRateFavorite(exchangeRateDto: ExchangeRateDto)


}