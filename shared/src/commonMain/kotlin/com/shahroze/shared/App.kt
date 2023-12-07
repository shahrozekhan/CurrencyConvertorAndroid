package com.shahroze.shared

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.shahroze.shared.theme.CurrencyConvertorTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(
    darkTheme: Boolean,
    dynamicColor: Boolean
) {
    CurrencyConvertorTheme(
        darkTheme,
        dynamicColor
    ) {
        var greetingText by remember { mutableStateOf("${getPlatform().name}: GREETING") }
        var showImage by remember { mutableStateOf(false) }

        Column(
            Modifier.fillMaxWidth(),  // [2]
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = {  // [3]
                greetingText = "${getPlatform().name}: GREETING"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {  // [4]
                Image(
                    painterResource("compose-multiplatform.xml"), // [5]
                    null
                )
            }
        }
//        ExchangeRateScreen()
    }

}