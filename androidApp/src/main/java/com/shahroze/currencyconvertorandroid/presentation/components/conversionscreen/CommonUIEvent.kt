package com.shahroze.currencyconvertorandroid.presentation.components.conversionscreen

import androidx.compose.material3.SnackbarDuration

sealed class CommonUIEvent {
    data class ShowSnackbar(
        val message: String,
        val duration: SnackbarDuration = SnackbarDuration.Long
    ) : CommonUIEvent()
}