package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

//This use case copies the combined response of symbols and exchange rate.
//saves the time stamp to preferences and exchange rate from 'exchangerates.json' file to databases.
class CopyExchangeRateFromAssetsCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository
) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> {

        return exchangeRateRepository.getExchangeRatesFromAssets()
            .transform {
                when (it) {
                    is Resource.Loading -> {
                        emit(Resource.Loading())
                    }

                    is Resource.Success -> {
                        it.data?.let { listDataDto ->
                            emit(Resource.Success(listDataDto.map { dataDto -> dataDto.toExchangeRate() }
                                .sortedBy { exchangeRate -> exchangeRate.currency }))
                        } ?: run {
                            emit(Resource.Error<List<ExchangeRate>>("No List Available"))
                        }
                    }

                    is Resource.Error -> {
                        emit(Resource.Error(it.message))
                    }
                }

            }.flowOn(Dispatchers.IO)
    }

}