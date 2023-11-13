package com.shahroze.currencyconvertorandroid.data.localdatasource.asset

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shahroze.currencyconvertorandroid.common.Constants
import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.utils.LocalExceptionParser
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
    @ApplicationContext val context: Context,
    val errorParser: LocalExceptionParser
) {
    inline fun <reified T : Any?> loadJsonFromAssets(
        fileName: String,
    ): Flow<Resource<T>> =
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
                    Gson().fromJson<T>(assetFile, object : TypeToken<T>() {}.type)

                emit(Resource.Success(exchangeRateJson))

            } catch (exception: Exception) {
                emit(
                    Resource.Error(
                        errorParser.logParseDisplayMessage(
                            exception,
                            this@AssetFileDataSource.javaClass.name
                        )
                    )
                )
            }
        }.onStart {
            emit(Resource.Loading())
        }.flowOn(Dispatchers.IO)

}