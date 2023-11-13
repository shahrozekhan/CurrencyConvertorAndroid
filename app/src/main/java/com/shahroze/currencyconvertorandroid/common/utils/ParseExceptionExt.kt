package com.shahroze.currencyconvertorandroid.common.utils

import android.content.Context
import android.util.Log
import com.google.gson.JsonSyntaxException
import com.shahroze.currencyconvertorandroid.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.FileNotFoundException
import javax.inject.Inject

class LocalExceptionParser @Inject constructor(@ApplicationContext private val context: Context) {
    fun logParseDisplayMessage(exception: Exception, className: String): String {
        Log.e(className, exception.message ?: exception.cause?.message ?: "")
        exception.printStackTrace()
        return when (exception) {
            is JsonSyntaxException -> {
                context.getString(R.string.unable_to_parse_error_occurred)
            }

            is FileNotFoundException -> {
                context.getString(R.string.file_not_error_occurred)
            }

            else -> {
                context.getString(R.string.unexpected_error_occurred)
            }
        }
    }

}