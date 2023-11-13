package com.shahroze.currencyconvertorandroid.common.utils

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate

fun buildExchangeRateListSortedByCurrency(
    rates: HashMap<String, Double>?,
    symbols: HashMap<String, String>?
): List<ExchangeRate> = buildList {
    rates?.onEach { exchangeRateDto ->
        symbols?.get(exchangeRateDto.key)?.let {
            add(
                ExchangeRate(
                    exchangeRateDto.key,
                    currencyName = it,
                    exchangeRateDto.value
                )
            )
        }
    }
}.sortedBy { exchangeRate -> exchangeRate.currency }

fun buildExchangeRateListSortedByCurrency(
    rates: HashMap<String, Double>?,
): List<ExchangeRate> = buildList {
    rates?.onEach { exchangeRateDto ->
        add(
            ExchangeRate(
                exchangeRateDto.key,
                currencyName = "",
                exchangeRateDto.value
            )
        )
    }
}.sortedBy { exchangeRate -> exchangeRate.currency }