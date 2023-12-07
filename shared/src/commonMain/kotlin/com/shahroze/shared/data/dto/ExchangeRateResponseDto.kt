package com.shahroze.shared.data.dto


import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponseDto(
    val base: String,
    val date: String,
    val rates: HashMap<String, Double>,
    val symbols: HashMap<String, String>,
    val success: Boolean,
    val timestamp: Int
)