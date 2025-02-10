package com.bruno13palhano.mais1venda.ui.screens.shared

fun stringToInt(value: String): Int {
    return try {
        value.toInt()
    } catch (e: Exception) {
        -1
    }
}

fun stringToFloat(value: String): Float {
    return try {
        value.toFloat()
    } catch (e: Exception) {
        -1f
    }
}
