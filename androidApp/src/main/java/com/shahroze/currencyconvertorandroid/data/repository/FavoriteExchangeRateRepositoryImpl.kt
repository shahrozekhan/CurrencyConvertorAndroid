package com.shahroze.currencyconvertorandroid.data.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import com.shahroze.currencyconvertorandroid.data.source.local.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.di.modules.Default
import com.shahroze.currencyconvertorandroid.domain.repository.FavoriteExchangeRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoriteExchangeRateRepositoryImpl
@Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    @Default private val defaultDispatcher: CoroutineDispatcher
) : FavoriteExchangeRateRepository {

    override suspend fun getFavoriteExchangeRates(): List<ExchangeRateDto> =
        withContext(defaultDispatcher) {
            exchangeRateDao.getFavoriteExchangeRates()
        }

    override suspend fun markExchangeRateFavorite(exchangeRateDto: ExchangeRateDto) =
        withContext(defaultDispatcher) {
            exchangeRateDao.update(t = exchangeRateDto)
        }

}