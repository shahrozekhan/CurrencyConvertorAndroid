package com.shahroze.currencyconvertorandroid.common.base

import com.shahroze.currencyconvertorandroid.data.remote.helper.ErrorResponse


sealed class RemoteResource<out T> {
    data class Success<out T>(val data: T) : RemoteResource<T>()
    sealed class ResourceError(open val throwable: Throwable) : RemoteResource<Nothing>() {
        data class GenericError(
            val error: ErrorResponse? = null,
            val exception: Throwable
        ) : ResourceError(exception)

        data class NetworkError(override val throwable: Throwable) : ResourceError(throwable)
    }
}