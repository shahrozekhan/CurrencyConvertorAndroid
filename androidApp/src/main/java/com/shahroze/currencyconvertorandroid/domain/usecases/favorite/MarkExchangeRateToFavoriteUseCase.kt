package com.shahroze.currencyconvertorandroid.domain.usecases.favorite

import com.shahroze.currencyconvertorandroid.di.modules.IO
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import com.shahroze.currencyconvertorandroid.domain.repository.FavoriteExchangeRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MarkExchangeRateToFavoriteUseCase @Inject constructor(
    private val favoriteExchangeRateRepository: FavoriteExchangeRateRepository,
    @IO private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        exchangeRate: ExchangeRate,
        listOfFavorite: List<ExchangeRate>?,
        isSelected: Boolean = true
    ): List<ExchangeRate> =
        withContext(dispatcher) {
            val mutableListOfFavorites = listOfFavorite?.toMutableList()
            if (isSelected) {
                if (mutableListOfFavorites?.filter { exchangeRate == it }
                        .isNullOrEmpty()) {
                    mutableListOfFavorites?.add(exchangeRate)
                }
            } else {
                val exchangeRateObj = mutableListOfFavorites?.find { exchangeRate == it }
                if (exchangeRateObj != null) {
                    mutableListOfFavorites.remove(exchangeRate)
                }
            }
            val exchangeRateDto = exchangeRate.toExchangeRateDto()
            exchangeRateDto.selected = isSelected
            favoriteExchangeRateRepository.markExchangeRateFavorite(exchangeRateDto = exchangeRateDto)
            mutableListOfFavorites?.toList() ?: listOf()
        }
}