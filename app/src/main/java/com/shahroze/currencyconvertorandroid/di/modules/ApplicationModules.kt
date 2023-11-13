package com.shahroze.currencyconvertorandroid.di.modules

import com.shahroze.currencyconvertorandroid.data.remote.repository.RemoteExchangeRateRepositoryImpl
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

}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryProviderModule {
    @Binds
    abstract fun bindExchangeRateRepository(remoteExchangeRateRepositoryImpl: RemoteExchangeRateRepositoryImpl): RemoteExchangeRateRepository

}