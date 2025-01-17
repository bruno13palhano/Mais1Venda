package com.bruno13palhano.data.datasource.local.data

import kotlinx.coroutines.flow.Flow

internal interface OrderLocalData<T> {
    suspend fun insert(order: T)

    suspend fun update(order: T)

    suspend fun delete(id: Long)

    suspend fun getById(id: Long): T?

    fun getAll(): Flow<List<T>>
}
