package com.shahroze.currencyconvertorandroid.presentation.components.currency

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate

sealed class ExchangeRateScreenEvent {
    data class EnteredAmount(val value: String) : ExchangeRateScreenEvent()
    data class SelectFromCurrency(val value: String) : ExchangeRateScreenEvent()
    data class SelectedToCurrency(val value: ExchangeRate) : ExchangeRateScreenEvent()
    data class RemoveCurrencyFromSelected(val index: Int, val value: ExchangeRate) :
        ExchangeRateScreenEvent()

    object ConvertExchangeRate : ExchangeRateScreenEvent()
}