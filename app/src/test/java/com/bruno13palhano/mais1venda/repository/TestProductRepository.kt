package com.bruno13palhano.mais1venda.repository

import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.repository.ProductRepository

internal class TestProductRepository(
    private val shouldReturnError: Boolean = false,
) : ProductRepository {
    private val products = mutableListOf<Product>()

    override suspend fun insert(product: Product): Resource<Boolean> {
        products.add(product)

        return if (shouldReturnError) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        } else {
            Resource.Success(true)
        }
    }

    override suspend fun update(product: Product): Resource<Boolean> {
        products.find { it.id == product.id }?.let {
            products.remove(it)
            products.add(product)
        }

        return if (shouldReturnError) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        } else {
            Resource.Success(true)
        }
    }

    override suspend fun delete(id: Long): Resource<Boolean> {
        products.removeIf { it.id == id }

        return if (shouldReturnError) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        } else {
            Resource.Success(true)
        }
    }

    override suspend fun getAll(): Resource<List<Product>> {
        return if (shouldReturnError) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        } else {
            Resource.Success(products)
        }
    }
}
