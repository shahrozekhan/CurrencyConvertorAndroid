package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetExchangeRateFromDataBase @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        val listOfExchangeRates = exchangeRateDao.getExchangeRates()
        if (listOfExchangeRates.isNotEmpty()) {

            emit(
                Resource.Success(
                    exchangeRateDao.getExchangeRates()
                        .map { domainDBDto -> domainDBDto.toExchangeRate() })
            )
        } else {
            emit(Resource.Error())
        }
    }.flowOn(Dispatchers.IO)
}