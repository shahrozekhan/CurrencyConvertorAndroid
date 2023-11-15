package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerate

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.source.remote.helper.RemoteErrorParser
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ForceSyncExchangeRatesUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val remoteErrorParser: RemoteErrorParser
) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> = flow {
        emit(Resource.Loading())
        when (val remoteResource = exchangeRateRepository.getExchangeRateFromRemote()) {
            is RemoteResource.Success -> {
                Resource.Success(remoteResource.data.map { dataDto -> dataDto.toExchangeRate() }
                    .sortedBy { it.currency })
            }

            is RemoteResource.ResourceError -> {
                Resource.Error(remoteErrorParser.parseErrorInfo(remoteResource))
            }
        }
    }

}