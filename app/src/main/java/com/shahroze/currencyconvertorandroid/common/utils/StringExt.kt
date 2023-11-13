package com.shahroze.currencyconvertorandroid.common.utils

val String.Companion.empty: String get() = ""

fun String.containsDigitsAndDecimalOnly(): Boolean {
    val check = Regex("[0-9]*(\\.[0-9]*)?")
    return check.matches(this)
}
