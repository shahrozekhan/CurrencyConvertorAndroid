package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.common.Constants.ExchangeRateConstants.DEFAULT_CURRENCY
import com.shahroze.currencyconvertorandroid.common.Response
import com.shahroze.currencyconvertorandroid.common.buildExchangeRateList
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import com.shahroze.currencyconvertorandroid.data.localdatasource.asset.AssetFileDataSource
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class CopyExchangeRateFromAssetsToDatabaseUseCase @Inject constructor(
    private val fileDataSource: AssetFileDataSource,
    private val exchangeRateDao: ExchangeRateDao
) {
    operator fun invoke(currency: String = DEFAULT_CURRENCY): Flow<Response<List<ExchangeRate>>> {

        return fileDataSource.loadJsonFromAssets(
            "exchangerate.json",
            ExchangeRateResponseDto::class.java
        )
            .zip(
                fileDataSource.loadJsonFromAssets(
                    "symbols.json",
                    ExchangeRateResponseDto::class.java
                )
            ) { exchangeRate, symbols ->
                if (exchangeRate is Response.Loading && symbols is Response.Loading) {
                    Response.Loading()
                } else if (exchangeRate is Response.Success && symbols is Response.Success) {
                    val listOfExchangeRate =
                        buildExchangeRateList(exchangeRate.data?.rates, symbols.data?.symbols)
                    exchangeRateDao.insertAll(listOfExchangeRate.map { domainDto -> domainDto.toExchangeRateDto() })
                    Response.Success(listOfExchangeRate)
                } else {
                    if (exchangeRate is Response.Error)
                        Response.Error(exchangeRate.message)
                    else {
                        Response.Error(symbols.message)
                    }
                }
            }
    }

}