package com.shahroze.currencyconvertorandroid.di.modules

import android.content.Context
import androidx.room.Room
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.ExchangeRateDataBase
import com.shahroze.currencyconvertorandroid.data.localdatasource.database.dao.ExchangeRateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    private const val databaseName = "AppDB"

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): ExchangeRateDataBase =
        Room.databaseBuilder(context, ExchangeRateDataBase::class.java, databaseName).build()

    @Provides
    fun provideExchangeRateDao(exchangeRateDataBase: ExchangeRateDataBase): ExchangeRateDao =
        exchangeRateDataBase.exchangeRateDao()
}