package com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.shahroze.currencyconvertorandroid.common.base.BaseDao
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto

@Dao
interface ExchangeRateDao : BaseDao<ExchangeRateDto> {
    @Query("SELECT * FROM exchange_rate ORDER BY currency ASC")
    fun getExchangeRates(
    ): List<ExchangeRateDto>

    @Query("SELECT * FROM exchange_rate WHERE selected = 1")
    fun getSelectedExchangeRates(
    ): List<ExchangeRateDto>

    @Query("DELETE FROM exchange_rate")
    fun deleteAll(): Int

    @Update
    override fun update(exchangeRateDto: ExchangeRateDto)
}