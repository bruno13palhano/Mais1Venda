package com.bruno13palhano.data.model.company

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Long,
    val name: String,
    val price: Float,
    val category: List<String>,
    val description: String,
    val code: String,
    val quantity: Int,
    val exhibitToCatalog: Boolean,
    val lastModifiedTimestamp: Long,
)
