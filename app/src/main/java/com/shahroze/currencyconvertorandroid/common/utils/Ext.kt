package com.shahroze.currencyconvertorandroid.common.utils

import java.math.BigDecimal
import java.math.RoundingMode

val String.Companion.empty: String get() = ""

fun String.containsDigitsAndDecimalOnly(): Boolean {
    val check = Regex("[0-9]*(\\.[0-9]*)?")
    return check.matches(this)
}

fun BigDecimal.roundUp(place: Int): BigDecimal = this.setScale(place, RoundingMode.UP)
fun Double.roundUpTwoDecimal(): Double = String.format("%.2f", this).toDouble()