package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.DatabaseExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteExchangeRateUseCase @Inject constructor(
    private val databaseExchangeRateRepository: DatabaseExchangeRateRepository
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        val listOfFavoriteExchangeRates = databaseExchangeRateRepository.getFavoriteExchangeRates()
        emit(Resource.Success(listOfFavoriteExchangeRates.map { dataDto ->
            dataDto.toExchangeRate()
        }))
    }.flowOn(Dispatchers.IO)
}