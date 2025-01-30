package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.company.asInternal
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteData: ProductRemoteData,
    private val productDao: ProductDao,
) : ProductRepository {
    override suspend fun insert(product: Product) {
        productDao.insert(product = product.asInternal()).let {
            val response = productRemoteData.insert(product.copy(id = it))
            response.data?.let { success ->
                if (success) {
                    // TODO: log info?
                } else {
                    // TODO: retry and log info?
                }
            }
        }
    }

    override suspend fun update(product: Product) {
        productDao.update(product = product.asInternal()).let {
            // whether the table was updated successfully
            if (it > 0) {
                val response = productRemoteData.update(product = product)
                response.data?.let { success ->
                    if (success) {
                        // TODO: log info?
                    } else {
                        // TODO: retry and log info?
                    }
                }
            } else {
                // TODO: retry and log info?
            }
        }
    }

    override suspend fun delete(id: Long) {
        productDao.delete(id = id).let {
            val response = productRemoteData.deleteById(id = id)
            response.data?.let { success ->
                if (success) {
                    // TODO: log info?
                } else {
                    // TODO: retry and log info?
                }
            }
        }
    }

    override suspend fun getAll(): Flow<List<Product>> {
        return productDao.getAll().map { it.map { product -> product.asExternal() } }
    }
}
