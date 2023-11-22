package com.shahroze.currencyconvertorandroid.data.source.remote.helper


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Error(
    @SerializedName("code")
    val code: Any,
    @SerializedName("info")
    val info: String,
    @SerializedName("message")
    val message: String
)