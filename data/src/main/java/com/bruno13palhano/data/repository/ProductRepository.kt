package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insert(product: Product)

    suspend fun update(product: Product)

    suspend fun delete(id: Long)

    suspend fun getAll(): Flow<List<Product>>
}
