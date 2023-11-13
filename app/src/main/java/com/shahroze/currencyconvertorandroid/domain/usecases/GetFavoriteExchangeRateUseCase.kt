package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFavoriteExchangeRateUseCase @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao
) {
    operator fun invoke() = flow {
        emit(Resource.Loading())
        emit(Resource.Success(exchangeRateDao.getSelectedExchangeRates().map { dataDto ->
            dataDto.toExchangeRate()
        }))
    }.flowOn(Dispatchers.IO)
}