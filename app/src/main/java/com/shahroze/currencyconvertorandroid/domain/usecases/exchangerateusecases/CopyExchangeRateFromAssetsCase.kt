package com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases

import com.shahroze.currencyconvertorandroid.common.base.Resource
import com.shahroze.currencyconvertorandroid.common.utils.buildExchangeRateListSortedByCurrency
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateResponseDto
import com.shahroze.currencyconvertorandroid.data.localdatasource.asset.AssetFileDataSource
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import com.shahroze.currencyconvertorandroid.data.localdatasource.preferences.AppPreferences
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import com.shahroze.currencyconvertorandroid.domain.model.toExchangeRateDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

//This use case copies the combined response of symbols and exchange rate.
//saves the time stamp to preferences and exchange rate from 'exchangerates.json' file to databases.
class CopyExchangeRateFromAssetsCase @Inject constructor(
    private val fileDataSource: AssetFileDataSource,
    private val exchangeRateDao: ExchangeRateDao,
    private val appPreferences: AppPreferences
) {
    operator fun invoke(): Flow<Resource<List<ExchangeRate>>> {

        return fileDataSource.loadJsonFromAssets<ExchangeRateResponseDto>(
            "exchangerate.json",
        ).zip(
            fileDataSource.loadJsonFromAssets<ExchangeRateResponseDto>(
                "symbols.json",
            )
        ) { exchangeRate, symbols ->
            if (exchangeRate is Resource.Loading && symbols is Resource.Loading) {
                Resource.Loading()
            } else if (exchangeRate is Resource.Success && symbols is Resource.Success) {
                val listOfExchangeRate =
                    buildExchangeRateListSortedByCurrency(exchangeRate.data?.rates, symbols.data?.symbols)
                exchangeRateDao.insertAll(listOfExchangeRate.map { domainDto -> domainDto.toExchangeRateDto() })
                appPreferences.timeStamp = exchangeRate.data?.timestamp.toString()
                Resource.Success(listOfExchangeRate)
            } else {
                if (exchangeRate is Resource.Error)
                    Resource.Error(exchangeRate.message)
                else {
                    Resource.Error(symbols.message)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

}