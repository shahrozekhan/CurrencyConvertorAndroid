package com.shahroze.shared.common

import com.shahroze.shared.data.dto.ExchangeRateDto

fun buildExchangeRateDtoListSortedByCurrency(
    rates: HashMap<String, Double>?,
    symbols: HashMap<String, String>?
): List<ExchangeRateDto> = buildList {
    rates?.onEach { exchangeRateDto ->
        symbols?.get(exchangeRateDto.key)?.let {
            add(
                ExchangeRateDto(
                    currency = exchangeRateDto.key,
                    currencyName = it,
                    rate = exchangeRateDto.value,
                    selected = false
                )
            )
        }
    }
}

fun buildExchangeRateDtoListSortedByCurrency(
    rates: HashMap<String, Double>?,
): List<ExchangeRateDto> = buildList {
    rates?.onEach { exchangeRateDto ->
        add(
            ExchangeRateDto(
                currency = exchangeRateDto.key,
                currencyName = "",
                rate = exchangeRateDto.value,
                selected = false
            )
        )
    }
}