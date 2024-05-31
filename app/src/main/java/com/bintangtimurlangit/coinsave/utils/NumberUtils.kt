package com.bintangtimurlangit.coinsave.utils

import java.text.DecimalFormat

// Function to simplify a number into a readable format (e.g., 1.5K, 2.3M)
fun simplifyNumber(value: Float): String {
    return when {
        value >= 1000 && value < 1_000_000 -> DecimalFormat("0.#K").format(value / 1000)
        value >= 1_000_000 -> DecimalFormat("0.#M").format(value / 1_000_000)
        else -> DecimalFormat("0.#").format(value)
    }
}
