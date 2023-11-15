package com.shahroze.currencyconvertorandroid.di.modules

import com.shahroze.currencyconvertorandroid.data.repository.ExchangeRateRepositoryImpl
import com.shahroze.currencyconvertorandroid.data.repository.FavoriteExchangeRateRepositoryImpl
import com.shahroze.currencyconvertorandroid.domain.repository.ExchangeRateRepository
import com.shahroze.currencyconvertorandroid.domain.repository.FavoriteExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @IO
    fun provideIODispatcher() = Dispatchers.IO

    @Provides
    @Default
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Provides
    @Main
    fun provideMainDispatcher() = Dispatchers.Main

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryProviderModule {
    @Binds
    abstract fun bindRemoteExchangeRateRepository(remoteExchangeRateRepositoryImpl: ExchangeRateRepositoryImpl): ExchangeRateRepository

    @Binds
    abstract fun bindDatabaseExchangeRateRepository(databaseExchangeRateRepositoryImpl: FavoriteExchangeRateRepositoryImpl): FavoriteExchangeRateRepository

}