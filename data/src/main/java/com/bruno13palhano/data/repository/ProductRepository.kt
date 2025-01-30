package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.resource.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun insert(product: Product): Resource<Boolean>

    suspend fun update(product: Product): Resource<Boolean>

    suspend fun delete(id: Long): Resource<Boolean>

    suspend fun getAll(): Resource<List<Product>>
}
