package com.bruno13palhano.data.datasource.local.data

import com.bruno13palhano.data.model.company.Product
import kotlinx.coroutines.flow.Flow

interface ProductLocalData {
    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(id: Long)

    suspend fun getAll(): Flow<List<Product>>
}
