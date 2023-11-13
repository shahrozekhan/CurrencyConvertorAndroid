package com.shahroze.currencyconvertorandroid.di.modules

import com.shahroze.currencyconvertorandroid.data.repository.DatabaseExchangeRateRepositoryImpl
import com.shahroze.currencyconvertorandroid.data.repository.RemoteExchangeRateRepositoryImpl
import com.shahroze.currencyconvertorandroid.domain.repository.DatabaseExchangeRateRepository
import com.shahroze.currencyconvertorandroid.domain.repository.RemoteExchangeRateRepository
import com.shahroze.currencyconvertorandroid.domain.usecases.LoadExchangeRateUsesCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.CopyExchangeRateFromAssetsCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.GetExchangeRateFromAPIUseCase
import com.shahroze.currencyconvertorandroid.domain.usecases.exchangerateusecases.GetExchangeRateFromDataBase
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
    fun provideLoadExchangeRatesUseCases(
        assetExchangeRate: CopyExchangeRateFromAssetsCase,
        remoteExchangeRate: GetExchangeRateFromAPIUseCase,
        databaseExchangeRate: GetExchangeRateFromDataBase

    ): LoadExchangeRateUsesCase {
        return LoadExchangeRateUsesCase(
            assetExchangeRate = assetExchangeRate,
            remoteExchangeRate = remoteExchangeRate,
            databaseExchangeRate = databaseExchangeRate
        )
    }

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
    abstract fun bindRemoteExchangeRateRepository(remoteExchangeRateRepositoryImpl: RemoteExchangeRateRepositoryImpl): RemoteExchangeRateRepository

    @Binds
    abstract fun bindDatabaseExchangeRateRepository(databaseExchangeRateRepositoryImpl: DatabaseExchangeRateRepositoryImpl): DatabaseExchangeRateRepository

}