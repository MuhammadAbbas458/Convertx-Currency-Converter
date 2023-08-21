package com.example.codechallenge.local

import android.app.Activity
import android.content.Context

const val LAST_UPDATE = "LAST_UPDATE"
const val RATES = "RATES"
const val CURRENCIES = "CURRENCIES"

const val PREF_RATES = "PREF_RATES"

fun Activity.getRates(): String? {
    return getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).getString(RATES, null)
}

fun Activity.setRates(rates: String?) = with(getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).edit()) {
    putString(RATES, rates)
    apply()
}

fun Activity.getCurrencies(): String? {
    return getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).getString(CURRENCIES, null)
}

fun Activity.setCurrencies(currencies: String?) = with(getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).edit()) {
    putString(CURRENCIES, currencies)
    apply()
}

fun Activity.getLastUpdateTime(): Long {
    return getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).getLong(LAST_UPDATE, 0)
}

fun Activity.setLastUpdateTime(time: Long) = with(getSharedPreferences(PREF_RATES, Context.MODE_PRIVATE).edit()) {
    putLong(LAST_UPDATE, time)
    apply()
}