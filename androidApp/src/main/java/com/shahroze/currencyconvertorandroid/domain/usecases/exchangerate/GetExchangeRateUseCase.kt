package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerate

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.enums.TimeStampState
import com.shahroze.currencyconvertorandroid.common.utils.TimeStampUtils
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.data.source.remote.helper.RemoteErrorParser
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRateUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val appPreferences: AppPreferences,
    private val remoteErrorParser: RemoteErrorParser
) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> = flow {
        emit(Resource.Loading())
        when (TimeStampUtils.getTimeStampEnum(appPreferences.timeStamp)) {
            TimeStampState.TODAY -> {
                loadFromDatabase()
            }

            TimeStampState.NOT_TODAY -> {
                loadFromRemote(onError = {
                    loadFromDatabase()
                })
            }

            TimeStampState.NOT_EXIST -> {
                loadFromRemote(onError = {
                    loadFromAsset()
                })
            }
        }
    }

    private suspend fun FlowCollector<Resource<List<ExchangeRate>>>.loadFromAsset() {
        when (val resource = exchangeRateRepository.getExchangeRatesFromAssets()) {
            is Resource.Success -> {
                resource.data?.let { listDataDto ->
                    emit(Resource.Success(listDataDto.map { dataDto -> dataDto.toExchangeRate() }
                        .sortedBy { exchangeRate -> exchangeRate.currency }))
                } ?: run {
                    emit(Resource.Error("No List Available"))
                }
            }

            is Resource.Error -> {
                emit(Resource.Error(resource.message))
            }

            is Resource.Loading -> {}
        }
    }


    private suspend fun FlowCollector<Resource<List<ExchangeRate>>>.loadFromRemote(onError: suspend () -> Unit = {}) {
        when (val remoteResource = exchangeRateRepository.getExchangeRateFromRemote()) {
            is RemoteResource.Success -> {
                emit(Resource.Success(remoteResource.data.map { dataDto -> dataDto.toExchangeRate() }
                    .sortedBy { exchangeRate -> exchangeRate.currency }))
            }

            is RemoteResource.ResourceError -> {
                emit(Resource.Error(remoteErrorParser.parseErrorInfo(remoteResource)))
                onError.invoke()
            }
        }
    }

    private suspend fun FlowCollector<Resource<List<ExchangeRate>>>.loadFromDatabase() {
        val listOfExchangeRates = exchangeRateRepository.getExchangeRatesFromDatabase()
        if (listOfExchangeRates.isNotEmpty()) {
            emit(Resource.Success(
                listOfExchangeRates
                    .map { dataDto -> dataDto.toExchangeRate() }
                    .sortedBy { exchangeRate -> exchangeRate.currency })
            )

        } else {
            emit(Resource.Error("No Currencies found!"))
        }
    }
}