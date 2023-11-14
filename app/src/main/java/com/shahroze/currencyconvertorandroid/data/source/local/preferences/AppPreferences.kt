package com.shahroze.currencyconvertorandroid.data.source.local.preferences

import android.content.Context
import android.content.SharedPreferences
import com.shahroze.currencyconvertorandroid.common.Constants.ExchangeRateConstants.DEFAULT_CURRENCY
import com.shahroze.currencyconvertorandroid.common.utils.empty
import com.shahroze.currencyconvertorandroid.common.utils.get
import com.shahroze.currencyconvertorandroid.common.utils.set
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.PreferenceKeys.BASE_CURRENCY
import com.shahroze.currencyconvertorandroid.data.source.local.preferences.PreferenceKeys.TIME_STAMP
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * this class is added as courotines are not working for our language change behaviour,
 * as we need to run a blocking call in on attach base context before onCreate
 */
private const val APP_PREF_NAME = "CURRENCY_CONVERTOR"

@Singleton
class AppPreferences @Inject constructor(@ApplicationContext context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(APP_PREF_NAME, 0)
    }

    var baseCurrency: String
        get() = prefs[BASE_CURRENCY, DEFAULT_CURRENCY]
        set(value) {
            prefs[BASE_CURRENCY] = value
        }

    var timeStamp: String
        get() = prefs[TIME_STAMP, String.empty]
        set(value) {
            prefs[TIME_STAMP] = value
        }
}