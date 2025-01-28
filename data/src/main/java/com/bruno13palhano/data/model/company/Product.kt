package com.bruno13palhano.data.model.company

import com.bruno13palhano.data.datasource.local.entity.ProductEntity
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
    val lastModifiedTimestamp: String,
)

internal fun Product.asInternal() = ProductEntity(
    id = id,
    name = name,
    price = price,
    category = category,
    description = description,
    code = code,
    quantity = quantity,
    exhibitToCatalog = exhibitToCatalog,
    lastModifiedTimestamp = lastModifiedTimestamp,
)
