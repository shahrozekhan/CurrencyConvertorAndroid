package com.shahroze.shared

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Typeface
import org.jetbrains.skia.FontStyle
import org.jetbrains.skia.Typeface

actual val FontConvergence:FontFamily = FontFamily(
    Typeface(loadCustomFont("font_convergence"))
)
private fun loadCustomFont(name: String): Typeface {
    return Typeface.makeFromName(name, FontStyle.NORMAL)
}