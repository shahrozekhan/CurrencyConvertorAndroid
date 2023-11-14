package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.FavoriteExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteExchangeRateUseCase @Inject constructor(
    private val favoriteExchangeRateRepository: FavoriteExchangeRateRepository
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        val listOfFavoriteExchangeRates = favoriteExchangeRateRepository.getFavoriteExchangeRates()
        if (listOfFavoriteExchangeRates.isEmpty()) {
            emit(Resource.Error("No favorites found! Please select currencies to mark favorite."))
        } else {
            emit(Resource.Success(listOfFavoriteExchangeRates.map { dataDto ->
                dataDto.toExchangeRate()
            }))
        }
    }.flowOn(Dispatchers.IO)
}