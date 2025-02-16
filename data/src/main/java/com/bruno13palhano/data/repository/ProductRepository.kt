package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insert(product: Product): Boolean
    suspend fun update(product: Product): Boolean
    suspend fun delete(id: Long): Boolean
    suspend fun get(id: Long): Product?
    suspend fun getAll(): Flow<List<Product>>
}
