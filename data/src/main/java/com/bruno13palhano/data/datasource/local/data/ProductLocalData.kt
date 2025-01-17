package com.bruno13palhano.data.datasource.local.data

import kotlinx.coroutines.flow.Flow

internal interface ProductLocalData<T> {
    suspend fun insert(product: T)

    suspend fun update(product: T)

    suspend fun delete(id: Long)

    suspend fun getById(id: Long): T?

    fun getAll(): Flow<List<T>>
}
