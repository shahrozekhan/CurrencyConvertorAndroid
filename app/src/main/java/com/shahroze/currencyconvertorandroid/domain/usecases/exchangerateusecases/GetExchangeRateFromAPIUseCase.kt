package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.utils.buildExchangeRateListSortedByCurrency
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.data.localdatasource.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.data.remote.helper.RemoteErrorParser
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import com.shahroze.currencyconvertorandroid.domain.repository.RemoteExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetExchangeRateFromAPIUseCase @Inject constructor(
    private val remoteExchangeRateRepository: RemoteExchangeRateRepository,
    private val exchangeRateDao: ExchangeRateDao,
    private val appPreferences: AppPreferences,
    private val remoteErrorParser: RemoteErrorParser
) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> {
        return remoteExchangeRateRepository.getExchangeRate()
            .zip(remoteExchangeRateRepository.getSymbols()) { exchangeRate, symbols ->
                when (exchangeRate) {
                    is RemoteResource.Success -> {
                        val listOfExchangeRate: List<ExchangeRate>
                        when (symbols) {
                            is RemoteResource.Success -> {
                                listOfExchangeRate = buildExchangeRateListSortedByCurrency(
                                    exchangeRate.data.rates,
                                    symbols.data.symbols
                                )
                            }

                            is RemoteResource.ResourceError -> {
                                listOfExchangeRate = buildExchangeRateListSortedByCurrency(exchangeRate.data.rates)
                                Resource.Success<List<ExchangeRate>>(listOf())
                            }
                        }
                        exchangeRateDao.insertAll(listOfExchangeRate.map { domainDto -> domainDto.toExchangeRateDto() })
                        appPreferences.timeStamp = exchangeRate.data.timestamp.toString()
                        Resource.Success(listOfExchangeRate)
                    }

                    is RemoteResource.ResourceError -> {
                        Resource.Error(remoteErrorParser.parseErrorInfo(exchangeRate))
                    }
                }

            }.flowOn(Dispatchers.IO)
    }


}