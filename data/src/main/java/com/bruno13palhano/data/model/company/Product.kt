package com.bruno13palhano.data.model.company

data class Product(
    val name: String,
    val price: Float,
    val category: List<String>,
    val description: String,
    val code: String,
    val quantity: Int,
    val exhibitToCatalog: Boolean,
    val lastModifiedTimestamp: Long,
)
