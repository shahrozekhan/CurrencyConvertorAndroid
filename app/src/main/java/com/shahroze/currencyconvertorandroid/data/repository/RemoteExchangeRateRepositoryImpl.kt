package com.shahroze.currencyconvertorandroid.data.repository

import com.shahroze.currencyconvertorandroid.data.remote.getway.ExchangeRateGateWay
import com.shahroze.currencyconvertorandroid.data.remote.helper.ServiceHelper
import com.shahroze.currencyconvertorandroid.domain.repository.RemoteExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

//Error Handling in Repository.
class RemoteExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateGateWay: ExchangeRateGateWay,
    private val serviceHelper: ServiceHelper,
) : RemoteExchangeRateRepository {
    override fun getExchangeRate() =
        flow {
            emit(serviceHelper.call { exchangeRateGateWay.getExchangeRate() })
        }.flowOn(Dispatchers.IO)

    override fun getSymbols() =
        flow {
            emit(serviceHelper.call { exchangeRateGateWay.getSymbols() })

        }.flowOn(Dispatchers.IO)
}