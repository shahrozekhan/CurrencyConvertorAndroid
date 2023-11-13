package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkExchangeRateToFavoriteUseCase @Inject constructor(private val exchangeRateDao: ExchangeRateDao) {
    suspend operator fun invoke(exchangeRate: ExchangeRate, isSelected: Boolean = true) =
        withContext(Dispatchers.IO) {
            val exchangeRateDto = exchangeRate.toExchangeRateDto()
            exchangeRateDto.selected = isSelected
            exchangeRateDao.update(exchangeRateDto = exchangeRateDto)
        }
}