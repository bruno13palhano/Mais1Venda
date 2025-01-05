package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Product
import kotlinx.coroutines.flow.Flow

internal class ProductRepositoryImpl : ProductRepository {
    override suspend fun insert(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun update(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }
}
