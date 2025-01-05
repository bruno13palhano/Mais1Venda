package com.bruno13palhano.data.model

data class Product(
    val name: String,
    val price: Float,
    val category: List<String>,
    val description: String,
    val code: String,
    val quantity: Int,
    val exhibitToCatalog: Boolean,
    val timestamp: Long,
)
