package com.shahroze.currencyconvertorandroid.domain.usecases.conversion

import com.shahroze.currencyconvertorandroid.common.utils.roundUp
import com.shahroze.currencyconvertorandroid.common.utils.roundUpFiveDecimal
import com.shahroze.currencyconvertorandroid.di.modules.IO
import com.shahroze.currencyconvertorandroid.domain.model.ExchangeRate
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import javax.inject.Inject

/*
    EUR = 1
    PKR = 302.242186 FROM
    INR = 89.252172  TO

    newRate = TO.rate / FROM.rate = 89.252172 / 302.242186 = 0.295300180233609
    convertedValue = newRate * amount

 */
class ConvertExchangeRateUseCase @Inject constructor(
    @IO private val dispatcher: CoroutineDispatcher
) {
    suspend operator fun invoke(
        amount: String,
        fromExchangeRate: ExchangeRate,
        toExchangeRateList: List<ExchangeRate>?
    ): List<Pair<ExchangeRate, BigDecimal>> = withContext(dispatcher) {
        buildList {
            toExchangeRateList?.forEach { exchangeRate ->
                val newRate = (exchangeRate.rate / fromExchangeRate.rate).roundUpFiveDecimal()

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
