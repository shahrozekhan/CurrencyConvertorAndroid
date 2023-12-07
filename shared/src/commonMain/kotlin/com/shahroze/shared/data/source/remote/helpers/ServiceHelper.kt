package com.shahroze.shared.data.source.remote.helpers

import com.shahroze.shared.common.RemoteResource

class ServiceHelper constructor(
    private val errorParser: RemoteErrorParser,
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