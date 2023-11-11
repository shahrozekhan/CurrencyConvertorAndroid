package com.shahroze.currencyconvertorandroid.common

import com.shahroze.currencyconvertorandroid.common.utils.empty

sealed class Response<out T>(val data: T? = null, val message: String) {
    class Success<T>(data: T) : Response<T>(data, String.empty)
    class Error<T>(message: String, data: T? = null) : Response<T>(data, message)
    class Loading<T>(data: T? = null) : Response<T>(data, String.empty)
}