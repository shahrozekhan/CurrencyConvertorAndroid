package com.shahroze.currencyconvertorandroid.data.source.remote.helper


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(
    @SerializedName("error")
    val error: Error,
    @SerializedName("success")
    val success: Boolean
)