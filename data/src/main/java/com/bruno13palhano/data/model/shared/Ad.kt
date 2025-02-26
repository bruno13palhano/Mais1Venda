package com.bruno13palhano.data.model.shared

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
    val unitsSold: Int,
    val questions: List<String>,
    val reviews: List<String>,
    val lastModifiedTimestamp: String,
)
