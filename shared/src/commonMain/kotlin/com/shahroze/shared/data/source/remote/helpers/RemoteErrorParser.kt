package com.shahroze.shared.data.source.remote.helpers

import com.shahroze.shared.data.source.remote.dto.Error
import com.shahroze.shared.data.source.remote.dto.ErrorResponse
import com.shahroze.shared.common.RemoteResource
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*
import kotlinx.serialization.json.Json

class RemoteErrorParser {

    suspend fun parseError(throwable: Throwable): RemoteResource.ResourceError {
        return when (throwable) {
            is IOException -> RemoteResource.ResourceError.NetworkError(throwable = throwable)
            is ResponseException -> {
                val errorResponse = convertErrorBody(throwable)
                return RemoteResource.ResourceError.GenericError(errorResponse, throwable)
            }

            else -> {
                RemoteResource.ResourceError.GenericError(
                    ErrorResponse(
                        error = Error(
                            code = "0",
                            info = throwable.message ?: "",
                            message = ""
                        ), success = false
                    ), throwable
                )
            }
        }
    }

    private suspend fun convertErrorBody(throwable: ResponseException): ErrorResponse? {
        val json: String? = throwable.response.body()
        return try {
            Json.decodeFromString<ErrorResponse>(json ?: "")
        } catch (exception: Exception) {
            null
        }
    }
}