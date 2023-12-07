package com.shahroze.shared.data.repository

import com.shahroze.shared.KMMContext
import com.shahroze.shared.KMMPreferences
import com.shahroze.shared.common.buildExchangeRateDtoListSortedByCurrency
import com.shahroze.shared.data.dto.ExchangeRateDto
import com.shahroze.shared.data.source.remote.gateway.ExchangeRateGateway
import com.shahroze.shared.common.RemoteResource
import com.shahroze.shared.data.source.local.PreferenceKeys
import com.shahroze.shared.data.source.remote.helpers.ServiceHelper
import com.shahroze.shared.domain.repository.ExchangeRateRepository

class ExchangeRateRepositoryImpl constructor(private val serviceHelper: ServiceHelper) :
    ExchangeRateRepository {

    private val exchangeRateGateWay = ExchangeRateGateway()
    override suspend fun getExchangeRateFromRemote(): RemoteResource<List<ExchangeRateDto>> {
        val exchangeRateRemoteResource =
            serviceHelper.call { exchangeRateGateWay.getExchangeRate() }
        val symbolsRemoteResource = serviceHelper.call { exchangeRateGateWay.getSymbols() }
        when (exchangeRateRemoteResource) {
            is RemoteResource.Success -> {
                val listOfExchangeRate: List<ExchangeRateDto> = when (symbolsRemoteResource) {
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
//                insertExchangeRatesToDatabase(listOfExchangeRate)
                KMMPreferences(context = KMMContext()).put(
                    PreferenceKeys.TIME_STAMP, exchangeRateRemoteResource.data.timestamp.toString()
                )
                return RemoteResource.Success(listOfExchangeRate)
            }

            is RemoteResource.ResourceError -> {
                return exchangeRateRemoteResource
            }
        }
    }

}