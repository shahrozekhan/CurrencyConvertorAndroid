package com.shahroze.currencyconvertorandroid.data.repository

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.utils.buildExchangeRateDtoListSortedByCurrency
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import com.shahroze.currencyconvertorandroid.data.source.local.asset.AssetFileHelper
import com.shahroze.currencyconvertorandroid.data.source.local.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.data.source.remote.getway.ExchangeRateGateWay
import com.shahroze.currencyconvertorandroid.data.source.remote.helper.ServiceHelper
import com.shahroze.currencyconvertorandroid.di.modules.Default
import com.shahroze.currencyconvertorandroid.di.modules.IO
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.withContext
import javax.inject.Inject

//Error Handling in Repository.
class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateGateWay: ExchangeRateGateWay,
    private val serviceHelper: ServiceHelper,
    private val exchangeRateDao: ExchangeRateDao,
    @Default private val defaultDispatcher: CoroutineDispatcher,
    @IO private val ioDispatcher: CoroutineDispatcher,
    private val fileDataSource: AssetFileHelper,
    private val appPreferences: AppPreferences
) : ExchangeRateRepository {
    override fun getFlowExchangeRateFromRemote() =
        getExchangeRate()
            .zip(getSymbols()) { exchangeRateRemoteResource, symbolsRemoteResource ->
                when (exchangeRateRemoteResource) {
                    is RemoteResource.Success -> {
                        val listOfExchangeRate: List<ExchangeRateDto> =
                            when (symbolsRemoteResource) {
                                is RemoteResource.Success -> {
                                    buildExchangeRateDtoListSortedByCurrency(
                                        exchangeRateRemoteResource.data.rates,
                                        symbolsRemoteResource.data.symbols
                                    )
                                }

                                is RemoteResource.ResourceError -> {
                                    buildExchangeRateDtoListSortedByCurrency(
                                        exchangeRateRemoteResource.data.rates
                                    )
                                }
                            }
                        insertExchangeRatesToDatabase(listOfExchangeRate)
                        appPreferences.timeStamp =
                            exchangeRateRemoteResource.data.timestamp.toString()
                        RemoteResource.Success(listOfExchangeRate)
                    }

                    is RemoteResource.ResourceError -> {
                        exchangeRateRemoteResource
                    }
                }

            }

    override suspend fun getExchangeRateFromRemote(): RemoteResource<List<ExchangeRateDto>> {
        val exchangeRateRemoteResource =
            serviceHelper.call { exchangeRateGateWay.getExchangeRate() }
        val symbolsRemoteResource = serviceHelper.call { exchangeRateGateWay.getSymbols() }
        when (exchangeRateRemoteResource) {
            is RemoteResource.Success -> {
                val listOfExchangeRate: List<ExchangeRateDto> =
                    when (symbolsRemoteResource) {
                        is RemoteResource.Success -> {
                            buildExchangeRateDtoListSortedByCurrency(
                                exchangeRateRemoteResource.data.rates,
                                symbolsRemoteResource.data.symbols
                            )
                        }

                        is RemoteResource.ResourceError -> {
                            buildExchangeRateDtoListSortedByCurrency(
                                exchangeRateRemoteResource.data.rates
                            )
                        }
                    }
                insertExchangeRatesToDatabase(listOfExchangeRate)
                appPreferences.timeStamp =
                    exchangeRateRemoteResource.data.timestamp.toString()
                return RemoteResource.Success(listOfExchangeRate)
            }

            is RemoteResource.ResourceError -> {
                return exchangeRateRemoteResource
            }
        }
    }

    override suspend fun getExchangeRatesFromAssets(): Resource<List<ExchangeRateDto>> {
        val exchangeRate = fileDataSource.loadJsonFromAssets<ExchangeRateResponseDto>(
            "exchangerate.json",
        )
        val symbols = fileDataSource.loadJsonFromAssets<ExchangeRateResponseDto>(
            "symbols.json",
        )
        return if (exchangeRate is Resource.Success && symbols is Resource.Success) {
            val listOfExchangeRate =
                buildExchangeRateDtoListSortedByCurrency(
                    exchangeRate.data?.rates,
                    symbols.data?.symbols
                )
            insertExchangeRatesToDatabase(listOfExchangeRate)
            appPreferences.timeStamp = exchangeRate.data?.timestamp.toString()
            Resource.Success(listOfExchangeRate)
        } else {
            if (exchangeRate is Resource.Error)
                Resource.Error(exchangeRate.message)
            else {
                Resource.Error(symbols.message)
            }
        }

    }

    private fun getExchangeRate() =
        flow {
            emit(serviceHelper.call { exchangeRateGateWay.getExchangeRate() })
        }.flowOn(ioDispatcher)

    private fun getSymbols() =
        flow {
            emit(serviceHelper.call { exchangeRateGateWay.getSymbols() })
        }.flowOn(ioDispatcher)

    override suspend fun insertExchangeRatesToDatabase(listOfExchangeRate: List<ExchangeRateDto>) =
        withContext(defaultDispatcher) {
            exchangeRateDao.getFavoriteExchangeRates().forEach { favorite ->
                listOfExchangeRate.find { favorite.currency == it.currency }?.selected =
                    favorite.selected
            }
            exchangeRateDao.insertAll(listOfExchangeRate)
            Unit
        }

    override suspend fun getExchangeRatesFromDatabase(): List<ExchangeRateDto> =
        withContext(defaultDispatcher) {
            exchangeRateDao.getExchangeRates()
        }

    override fun getFlowExchangeRatesFromAssets(): Flow<Resource<List<ExchangeRateDto>>> {
        return fileDataSource.loadFlowJsonFromAssets<ExchangeRateResponseDto>(
            "exchangerate.json",
        ).zip(
            fileDataSource.loadFlowJsonFromAssets<ExchangeRateResponseDto>(
                "symbols.json",
            )
        ) { exchangeRate, symbols ->
            if (exchangeRate is Resource.Loading && symbols is Resource.Loading) {
                Resource.Loading()
            } else if (exchangeRate is Resource.Success && symbols is Resource.Success) {
                val listOfExchangeRate =
                    buildExchangeRateDtoListSortedByCurrency(
                        exchangeRate.data?.rates,
                        symbols.data?.symbols
                    )
                insertExchangeRatesToDatabase(listOfExchangeRate)
                appPreferences.timeStamp = exchangeRate.data?.timestamp.toString()
                Resource.Success(listOfExchangeRate)
            } else {
                if (exchangeRate is Resource.Error)
                    Resource.Error(exchangeRate.message)
                else {
                    Resource.Error(symbols.message)
                }
            }
        }
    }
}