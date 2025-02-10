package com.bruno13palhano.mais1venda.ui.screens.products.shared

fun isPriceValid(price: Float) = price > 0

fun isQuantityValid(quantity: Int) = quantity > 0

fun isCodeValid(code: String) = code.length == 13
