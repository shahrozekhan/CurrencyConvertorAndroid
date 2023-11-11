package com.shahroze.currencyconvertorandroid.data.localdatasource.asset

import android.content.Context
import com.google.gson.Gson
import com.shahroze.currencyconvertorandroid.common.Constants
import com.shahroze.currencyconvertorandroid.common.Response
import com.shahroze.currencyconvertorandroid.common.utils.ErrorParser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import java.io.InputStream
import java.nio.charset.Charset
import javax.inject.Inject

class AssetFileDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val errorParser: ErrorParser
) {
    fun <T> loadJsonFromAssets(fileName: String,clazz: Class<T>): Flow<Response<T>> =
        flow {
            try {

                val assetFile: String?

                val inputStream: InputStream = context.assets.open(fileName)
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()

                assetFile = String(buffer, Charset.forName(Constants.UTF_8))

                val exchangeRateJson =
                    Gson().fromJson(assetFile,clazz)

                emit(Response.Success(exchangeRateJson))

            } catch (exception: Exception) {
                emit(
                    Response.Error(
                        errorParser.logParseDisplayMessage(
                            exception,
                            this@AssetFileDataSource.javaClass.name
                        )
                    )
                )
            }
        }.onStart {
            emit(Response.Loading())
        }.flowOn(Dispatchers.IO)

}