package com.shahroze.currencyconvertorandroid.data.remote.helper

import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import javax.inject.Inject

class ServiceHelper @Inject constructor(
    val errorParser: RemoteErrorParser,
) {
    suspend fun <T> call(
        apiCall: suspend () -> T
    ): RemoteResource<T> {
        return try {
            RemoteResource.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            errorParser.parseError(throwable = throwable)
        }
    }
}