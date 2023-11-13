package com.shahroze.currencyconvertorandroid.common.base

import com.shahroze.currencyconvertorandroid.common.utils.empty

sealed class Resource<out T>(val data: T? = null, val message: String) {
    class Success<T>(data: T) : Resource<T>(data, String.empty)
    class Error<T>(message: String? = "", data: T? = null) : Resource<T>(data, message ?: "")
    class Loading<T>(data: T? = null) : Resource<T>(data, String.empty)
}