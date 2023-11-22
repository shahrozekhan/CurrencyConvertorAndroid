package com.shahroze.currencyconvertorandroid.data.source.remote.helper

import android.content.Context
import com.google.gson.Gson
import com.shahroze.currencyconvertorandroid.R
import com.shahroze.currencyconvertorandroid.common.base.RemoteResource
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RemoteErrorParser @Inject constructor(@ApplicationContext private val context: Context) {

    fun parseError(throwable: Throwable): RemoteResource.ResourceError {
        return when (throwable) {
            is IOException -> RemoteResource.ResourceError.NetworkError(throwable = throwable)
            is HttpException -> {
                val errorResponse = convertErrorBody(throwable)
                return RemoteResource.ResourceError.GenericError(errorResponse, throwable)
            }

            else -> {
                RemoteResource.ResourceError.GenericError(
                    ErrorResponse(
                        error = Error(
                            code = 0,
                            info = throwable.message ?: "",
                            message = ""
                        ), success = false
                    ), throwable
                )
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
        val json: String? = throwable.response()?.errorBody()?.string()
        return try {
            Gson().fromJson(json, ErrorResponse::class.java)
        } catch (exception: Exception) {
            null
        }
    }

    fun parseErrorInfo(errorResource: RemoteResource.ResourceError): String {
        return when (errorResource) {
            is RemoteResource.ResourceError.GenericError -> {
                errorResource.error?.let {
                    if (errorResource.error.error.message.isNotEmpty()) {
                        return errorResource.error.error.message
                    }
                    return errorResource.error.error.info
                } ?: run {
                    return context.getString(R.string.generic_error)
                }
            }

            else -> {
                context.getString(R.string.no_network)
            }
        }
    }
}