package com.bruno13palhano.mais1venda.repository

import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

internal class TestProductRepository(
    private val shouldReturnError: Boolean = false,
) : ProductRepository {
    private val products = mutableListOf<Product>()

    override suspend fun insert(product: Product): Boolean {
        products.add(product)

        return !shouldReturnError
    }

    override suspend fun update(product: Product): Boolean {
        products.find { it.id == product.id }?.let {
            products.remove(it)
            products.add(product)
        }

        return !shouldReturnError
    }

    override suspend fun delete(id: Long): Boolean {
        products.removeIf { it.id == id }

        return !shouldReturnError
    }

    override suspend fun get(id: Long): Product? {
        return if (shouldReturnError) {
            null
        } else {
            products.find { it.id == id }
        }
    }

    override fun getAll(): Flow<List<Product>> {
        return if (shouldReturnError) {
            emptyFlow()
        } else {
            flowOf(products)
        }
    }
}
