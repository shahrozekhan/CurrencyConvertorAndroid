package com.shahroze.currencyconvertorandroid.data.source.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.shahroze.currencyconvertorandroid.common.base.BaseDao
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto

@Dao
interface ExchangeRateDao : BaseDao<ExchangeRateDto> {
    @Query("SELECT * FROM exchange_rate ORDER BY currency ASC")
    fun getExchangeRates(
    ): List<ExchangeRateDto>

    @Query("SELECT * FROM exchange_rate WHERE selected = 1")
    fun getFavoriteExchangeRates(
    ): List<ExchangeRateDto>

    @Query("DELETE FROM exchange_rate")
    fun deleteAll(): Int

}