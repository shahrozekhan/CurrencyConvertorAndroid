package com.shahroze.currencyconvertorandroid.data.repository

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.di.modules.Default
import com.shahroze.currencyconvertorandroid.domain.repository.DatabaseExchangeRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DatabaseExchangeRateRepositoryImpl
@Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    @Default private val defaultDispatcher: CoroutineDispatcher
) : DatabaseExchangeRateRepository {
    override suspend fun insertExchangeRates(listOfExchangeRate: List<ExchangeRateDto>) =
        withContext(defaultDispatcher) {

        }

    override suspend fun getExchangeRates(): List<ExchangeRateDto> =
        withContext(defaultDispatcher) {
            exchangeRateDao.getExchangeRates()
        }

    override suspend fun getFavoriteExchangeRates(): List<ExchangeRateDto> =
        withContext(defaultDispatcher)
        {
            exchangeRateDao.getFavoriteExchangeRates()
        }

    override suspend fun markExchangeRateFavorite(exchangeRateDto: ExchangeRateDto) =
        withContext(defaultDispatcher) {
            exchangeRateDao.update(t = exchangeRateDto)
        }

}