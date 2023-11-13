package com.shahroze.currencyconvertorandroid.domain.usecases

import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

/*
    EUR = 1
    PKR = 302.242186 FROM
    INR = 89.252172  TO

    newRate = TO.rate / FROM.rate = 89.252172 / 302.242186 = 0.295300180233609
    convertedValue = newRate * amount

 */
class ConvertExchangeRateUseCase @Inject constructor() {
    suspend operator fun invoke(
        amount: String,
        fromExchangeRate: ExchangeRate,
        toExchangeRateList: List<ExchangeRate>?
    ): List<Pair<ExchangeRate, BigDecimal>> = withContext(Dispatchers.IO) {
        buildList {
            toExchangeRateList?.forEach { exchangeRate ->
                val newRate = (exchangeRate.rate / fromExchangeRate.rate).roundUpTwoDecimal()

                val convertedExchangeRate =
                    ExchangeRate(exchangeRate.currency, exchangeRate.currencyName, newRate)

                add(
                    Pair(
                        convertedExchangeRate,
                        amount.toBigDecimal().multiply(BigDecimal(newRate)).roundUp(2)
                    )
                )
            }
        }
    }

}

fun BigDecimal.roundUp(place: Int): BigDecimal = this.setScale(place, RoundingMode.UP)
fun Double.roundUpTwoDecimal(): Double = String.format("%.2f", this).toDouble()
