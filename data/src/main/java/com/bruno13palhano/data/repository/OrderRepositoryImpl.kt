package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.shared.Order
import kotlinx.coroutines.flow.Flow

internal class OrderRepositoryImpl : OrderRepository {
    override suspend fun insert(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun update(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<List<Order>> {
        TODO("Not yet implemented")
    }
}
