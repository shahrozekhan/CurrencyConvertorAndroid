package com.shahroze.currencyconvertorandroid.data.dto


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ExchangeRateResponseDto(
    @SerializedName("MR/base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: HashMap<String, Double>,
    @SerializedName("symbols")
    val symbols: HashMap<String, String>,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
)