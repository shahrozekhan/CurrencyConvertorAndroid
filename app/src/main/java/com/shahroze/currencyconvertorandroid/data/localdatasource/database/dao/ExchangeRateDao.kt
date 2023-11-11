package com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao

import androidx.room.Query
import com.shahroze.currencyconvertorandroid.base.BaseDao
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import kotlinx.coroutines.flow.Flow

interface ExchangeRateDao : BaseDao<ExchangeRateDto> {
    @Query("SELECT * FROM exchange_rate")
    fun getExchangeRates(
    ): Flow<List<ExchangeRateDto>>

    @Query("DELETE FROM exchange_rate")
    fun deleteAll(): Int
}