package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.data.source.remote.helper.RemoteErrorParser
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRate
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class GetExchangeRateFromAPIUseCase @Inject constructor(
    private val exchangeRateRepository: ExchangeRateRepository,
    private val remoteErrorParser: RemoteErrorParser

) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> {
        return exchangeRateRepository.getExchangeRateFromRemote()
            .transform {
                when (it) {
                    is RemoteResource.Success -> {
                        emit(Resource.Success(it.data.map { dataDto -> dataDto.toExchangeRate() }
                            .sortedBy { exchangeRate -> exchangeRate.currency }))
                    }

                    is RemoteResource.ResourceError -> {
                        emit(Resource.Error(remoteErrorParser.parseErrorInfo(it)))
                    }
                }
            }.onStart { emit(Resource.Loading()) }
            .flowOn(Dispatchers.IO)
    }


}