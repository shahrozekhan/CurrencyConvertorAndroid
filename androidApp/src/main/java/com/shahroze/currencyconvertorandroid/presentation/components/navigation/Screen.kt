package com.shahroze.currencyconvertorandroid.presentation.components.navigation

import androidx.annotation.StringRes
import com.shahroze.currencyconvertorandroid.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    data object CurrencyComponent : Screen("exchange_rate_component", R.string.currency)
    data object CurrencyTrendComponent : Screen("currency_trend_component", R.string.trend)
}