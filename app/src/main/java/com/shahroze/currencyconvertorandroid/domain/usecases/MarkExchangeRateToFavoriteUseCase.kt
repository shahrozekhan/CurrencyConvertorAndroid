package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.di.modules.IO
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import com.shahroze.currencyconvertorandroid.domain.repository.DatabaseExchangeRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkExchangeRateToFavoriteUseCase @Inject constructor(
    private val databaseExchangeRateRepository: DatabaseExchangeRateRepository,
    @IO private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(exchangeRate: ExchangeRate, isSelected: Boolean = true) =
        withContext(dispatcher) {
            val exchangeRateDto = exchangeRate.toExchangeRateDto()
            exchangeRateDto.selected = isSelected
            databaseExchangeRateRepository.markExchangeRateFavorite(exchangeRateDto = exchangeRateDto)
        }
}