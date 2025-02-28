package com.bruno13palhano.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ad")
internal data class AdEntity(
    @PrimaryKey
    val id: Long,
    val title: String,
    val productId: Long,
    val price: Double,
    val description: String,
    val observations: String,
    val off: Float,
    val stockQuantity: Int,
    val lastModifiedTimestamp: String,
)
