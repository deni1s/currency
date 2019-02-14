package com.example.currency.models

import java.util.*
import java.util.concurrent.TimeUnit


class CurrencyPair {
    val symbol: String = ""
    val price: Float = 0F
    val bid: Float = 0F
    val ask: Float = 0F
    val timestamp: Long = 0

    fun getSecondsPassed(): Long {
        try {
            return TimeUnit.SECONDS.convert(
                Date().time / 1000 - timestamp,
                TimeUnit.SECONDS
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    override fun equals(other: Any?): Boolean {
        return other != null && other is CurrencyPair && other.symbol == symbol
    }
}