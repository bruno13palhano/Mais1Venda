package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun get(id: Long): Order?
    fun getAll(): Flow<List<Order>>
    suspend fun getNewOrders(): Resource<List<Order>>
    suspend fun confirmOrder(id: Long): Resource<Boolean>
    suspend fun cancelOrder(id: Long): Resource<Boolean>
}
