package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetExchangeRateFromDataBase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        val listOfExchangeRates = exchangeRateRepository.getExchangeRatesFromDatabase()
        if (listOfExchangeRates.isNotEmpty()) {
            emit(
                Resource.Success(
                    listOfExchangeRates
                        .map { dataDto -> dataDto.toExchangeRate() }
                        .sortedBy { exchangeRate -> exchangeRate.currency })
            )
        } else {
            emit(Resource.Error("No Currencies found!"))
        }
    }.flowOn(Dispatchers.IO)
}