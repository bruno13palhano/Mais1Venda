package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.shared.Order
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class OrderRepositoryImpl @Inject constructor() : OrderRepository {
    override suspend fun insert(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun update(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Long): Order? {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<List<Order>> {
        TODO("Not yet implemented")
    }
}
