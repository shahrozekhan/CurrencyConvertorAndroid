package com.shahroze.currencyconvertorandroid.domain.model

import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto

data class ExchangeRate(val currency: String, val country: String, val rate: Double)

fun ExchangeRate.toExchangeRateDto():ExchangeRateDto{
    return ExchangeRateDto(
        currency,
        country,
        rate
    )
}