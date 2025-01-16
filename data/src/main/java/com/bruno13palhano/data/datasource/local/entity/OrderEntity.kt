package com.bruno13palhano.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.data.model.shared.OrderStatus

@Entity(tableName = "order")
internal data class OrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val productId: Long,
    val customerUid: String,
    val orderDate: Long,
    val deliveryDate: Long,
    val status: OrderStatus,
    val lastModifiedTimestamp: Long,
)
