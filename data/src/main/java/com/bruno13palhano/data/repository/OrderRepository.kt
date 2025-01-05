package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.shared.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun insert(order: Order)

    suspend fun update(order: Order)

    suspend fun delete(order: Order)

    suspend fun getAll(): Flow<List<Order>>
}
