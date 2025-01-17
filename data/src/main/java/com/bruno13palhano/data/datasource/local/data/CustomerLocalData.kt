package com.bruno13palhano.data.datasource.local.data

import kotlinx.coroutines.flow.Flow

internal interface CustomerLocalData<T> {
    suspend fun insert(customer: T)

    suspend fun update(customer: T)

    suspend fun delete(uid: String)

    suspend fun getById(uid: String): T?

    fun getAll(): Flow<List<T>>
}
