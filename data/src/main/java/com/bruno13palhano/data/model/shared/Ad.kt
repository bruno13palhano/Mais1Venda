package com.bruno13palhano.data.model.shared

import com.bruno13palhano.data.datasource.local.entity.AdEntity
import com.bruno13palhano.data.model.company.Product

data class Ad(
    val id: Long,
    val title: String,
    val product: Product,
    val price: Double,
    val description: String,
    val observations: String,
    val off: Float,
    val stockQuantity: Int,
    val lastModifiedTimestamp: String,
)

internal fun Ad.asInternal() = AdEntity(
    id = id,
    title = title,
    productId = product.id,
    price = price,
    description = description,
    observations = observations,
    off = off,
    stockQuantity = stockQuantity,
    lastModifiedTimestamp = lastModifiedTimestamp,
)
