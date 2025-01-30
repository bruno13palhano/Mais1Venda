package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.resource.Resource
import javax.inject.Inject

internal class ProductRemoteData @Inject constructor(
    private val api: ApiService,
) {
    suspend fun insert(product: Product): Resource<Boolean> {
        return safeApiCall { api.insertProduct(product = product) }
    }

    suspend fun update(product: Product): Resource<Boolean> {
        return safeApiCall { api.updateProduct(product = product) }
    }

    suspend fun deleteById(id: Long): Resource<Boolean> {
        return safeApiCall { api.deleteProductById(id = id) }
    }

    suspend fun getAll(): Resource<List<Product>> {
        return safeApiCall { api.getAllProducts() }
    }
}
