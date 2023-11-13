package com.shahroze.currencyconvertorandroid.data.remote.helper


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Error(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val info: String
)