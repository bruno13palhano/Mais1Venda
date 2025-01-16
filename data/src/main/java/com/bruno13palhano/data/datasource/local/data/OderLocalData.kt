package com.bruno13palhano.data.datasource.local.data

import com.bruno13palhano.data.model.shared.Order
import kotlinx.coroutines.flow.Flow

interface OderLocalData {
    suspend fun insert(order: Order)

    suspend fun update(order: Order)

    suspend fun delete(id: Long)

    suspend fun getAll(): Flow<List<Order>>
}
