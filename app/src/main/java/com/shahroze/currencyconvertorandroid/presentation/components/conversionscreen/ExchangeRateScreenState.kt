package com.shahroze.currencyconvertorandroid.presentation.components.conversionscreen

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.appDefaultExchangeRate
import java.math.BigDecimal

data class ExchangeRateScreenState(
    val isCurrenciesLoading: Boolean = false,
    val isFavoriteLoading: Boolean = false,
    val isConverting: Boolean = false,
    val isForceSyncingExchangeRate: Boolean = false,
    val listOfCurrency: List<ExchangeRate>? = listOf(),
    val favoriteCurrencies: List<ExchangeRate>? = listOf(),
    val listOfConvertedAgainstBase: List<Pair<ExchangeRate, BigDecimal>> = listOf(),
    val fromCurrency: ExchangeRate = ExchangeRate.appDefaultExchangeRate,
    val errorMessage: String = "",
    val amount: String = ""
)
