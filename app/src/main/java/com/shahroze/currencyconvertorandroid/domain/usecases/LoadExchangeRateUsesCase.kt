package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.CopyExchangeRateFromAssetsCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.GetExchangeRateFromAPIUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.GetExchangeRateFromDataBase

data class LoadExchangeRateUsesCase(
    val assetExchangeRate: CopyExchangeRateFromAssetsCase,
    val remoteExchangeRate: GetExchangeRateFromAPIUseCase,
    val databaseExchangeRate: GetExchangeRateFromDataBase
)
