package com.shahroze.currencyconvertorandroid.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.shahroze.currencyconvertorandroid.ui.components.currency.ExchangeRateViewModel
import com.shahroze.currencyconvertorandroid.ui.theme.CurrencyConvertorAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConvertorAndroidTheme {
                val exchangeRateViewModel: ExchangeRateViewModel = hiltViewModel()
                val currencyRateState = exchangeRateViewModel.currencyRateState.value

                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier) {
                        if (currencyRateState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .width(64.dp),
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 4.dp,
                            )
                        }
                        if (!currencyRateState.isLoading && !currencyRateState.listOfCurrency.isNullOrEmpty()) {
                            DisplayText(
                                currencyRateState.listOfCurrency.joinToString("\n") { currencyRate ->
                                    "${currencyRate.currency} ${currencyRate.country} ${currencyRate.rate}" ?: ""
                                } ?: "")
                        } else if (!currencyRateState.errorMessage.isNullOrEmpty()) {
                            DisplayText(currencyRateState.errorMessage ?: "")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayText(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name!", modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CurrencyConvertorAndroidTheme {
        DisplayText("Android")
    }
}