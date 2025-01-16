package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Product
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class ProductRepositoryImpl @Inject constructor() : ProductRepository {
    override suspend fun insert(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun update(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<List<Product>> {
        TODO("Not yet implemented")
    }
}
