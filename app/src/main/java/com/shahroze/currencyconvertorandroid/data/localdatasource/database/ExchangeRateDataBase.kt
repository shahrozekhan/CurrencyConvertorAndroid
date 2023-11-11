package com.shahroze.currencyconvertorandroid.data.localdatasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shahroze.currencyconvertorandroid.data.dto.ExchangeRateDto
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao

@Database(entities = [ExchangeRateDto::class], version = 1)
abstract class ExchangeRateDataBase : RoomDatabase() {
    abstract fun exchangeRateDao(): ExchangeRateDao
}