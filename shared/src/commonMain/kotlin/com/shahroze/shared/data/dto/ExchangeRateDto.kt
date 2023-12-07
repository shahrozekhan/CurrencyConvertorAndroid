package com.shahroze.shared.data.dto

data class ExchangeRateDto(
    val currency: String,
    val currencyName: String,
    val rate: Double,
    var selected: Boolean
)