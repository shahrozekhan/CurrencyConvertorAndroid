package com.shahroze.shared.domain.repository

import com.shahroze.shared.data.dto.ExchangeRateDto
import com.shahroze.shared.common.RemoteResource

interface ExchangeRateRepository {
    suspend fun getExchangeRateFromRemote(): RemoteResource<List<ExchangeRateDto>>
}