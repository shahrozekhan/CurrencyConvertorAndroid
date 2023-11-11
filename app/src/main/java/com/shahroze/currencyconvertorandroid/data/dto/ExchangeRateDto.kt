package com.shahroze.currencyconvertorandroid.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rate")
data class ExchangeRateDto(@PrimaryKey val currency: String, val country: String, val rate: Double)