package com.shahroze.currencyconvertorandroid.common

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate

fun buildExchangeRateList(
    rates: HashMap<String, Double>?,
    symbols: HashMap<String, String>?
): List<ExchangeRate> = buildList {
    rates?.onEach { exchangeRateDto ->
        symbols?.get(exchangeRateDto.key)?.let {
            add(
                ExchangeRate(
                    exchangeRateDto.key,
                    country = it,
                    exchangeRateDto.value
                )
            )
        }
    }
}