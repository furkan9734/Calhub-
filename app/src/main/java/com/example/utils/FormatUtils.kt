package com.example.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object FormatUtils {
    fun formatNumber(value: Double, decimals: Int = 2, trimTrailingZeros: Boolean = false): String {
        if (value.isNaN()) return "0"
        if (value.isInfinite()) return if (value > 0) "Infinity" else "-Infinity"
        if (Math.abs(value) >= 1e15 || (Math.abs(value) > 0 && Math.abs(value) < 1e-6)) {
            val df = DecimalFormat("0.######E0", DecimalFormatSymbols(Locale.US))
            return df.format(value)
        }

        val pattern = if (trimTrailingZeros) {
            "#,##0." + "#".repeat(decimals)
        } else {
            "#,##0." + "0".repeat(decimals)
        }
        val df = DecimalFormat(pattern, DecimalFormatSymbols(Locale.US))
        val formatted = df.format(value)
        return if (formatted.endsWith(".")) formatted.substring(0, formatted.length - 1) else formatted
    }

    fun formatSmart(value: Double, maxDecimals: Int = 4): String {
        if (value.isNaN()) return "0"
        if (value.isInfinite()) return if (value > 0) "Overflow" else "-Overflow"
        if (value == value.toLong().toDouble() && Math.abs(value) < 1e12) {
            val df = DecimalFormat("#,##0", DecimalFormatSymbols(Locale.US))
            return df.format(value.toLong())
        }
        return formatNumber(value, decimals = maxDecimals, trimTrailingZeros = true)
    }

    fun parseNumberOrNull(input: String): Double? {
        val clean = input.trim().replace(",", "")
        return clean.toDoubleOrNull()
    }
}
