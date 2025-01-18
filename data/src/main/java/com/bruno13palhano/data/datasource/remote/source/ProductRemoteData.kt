package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.model.company.Product

class ProductRemoteData {
    suspend fun insert(product: Product): Product {
        TODO("Not yet implemented")
    }

    suspend fun update(product: Product): Product {
        TODO("Not yet implemented")
    }

    suspend fun deleteById(id: Long) {
        TODO("Not yet implemented")
    }

    suspend fun getById(id: Long): Product? {
        TODO("Not yet implemented")
    }

    suspend fun getAll(): List<Product> {
        TODO("Not yet implemented")
    }
}
