package com.shahroze.shared.theme

import androidx.compose.runtime.Composable

@Composable
expect fun CurrencyConvertorTheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    content: @Composable () -> Unit
)