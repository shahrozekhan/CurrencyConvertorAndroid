package com.shahroze.currencyconvertorandroid.ui.components.currency

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate

data class CurrencyRateState(
    val isLoading: Boolean = false,
    val listOfCurrency: List<ExchangeRate>? = listOf(),
    val errorMessage: String? = null
)
