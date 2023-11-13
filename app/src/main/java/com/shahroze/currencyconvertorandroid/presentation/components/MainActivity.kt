package com.shahroze.currencyconvertorandroid.presentation.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shahroze.currencyconvertorandroid.presentation.components.currency.ExchangeRateScreen
import com.shahroze.currencyconvertorandroid.presentation.components.navigation.Screen
import com.shahroze.currencyconvertorandroid.presentation.theme.CurrencyConvertorAndroidTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConvertorAndroidTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.CurrencyComponent.route
                ) {
                    composable(Screen.CurrencyComponent.route) { ExchangeRateScreen() }
                }
            }
        }
    }
}