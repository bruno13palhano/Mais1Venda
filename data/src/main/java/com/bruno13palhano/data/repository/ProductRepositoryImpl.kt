package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.repository.shared.remoteCallWithRetry
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteData: ProductRemoteData,
    private val productDao: ProductDao,
) : ProductRepository {
    override suspend fun insert(product: Product): Boolean {
        return try {
            val id = productDao.insert(product = product.asInternal())

            if (id <= 0) return false

            remoteCallWithRetry(
                call = { productRemoteData.insert(product = product.copy(id = id)) },
                error = {
                    // TODO: notify out of sync?
                },
            )

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun update(product: Product): Boolean {
        return try {
            val rowsAffected = productDao.update(product = product.asInternal())

            if (rowsAffected <= 0) return false

            remoteCallWithRetry(
                call = { productRemoteData.update(product = product) },
                error = {
                    // TODO: notify out of sync?
                },
            )

            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun get(id: Long): Product? {
        return try {
            productDao.getById(id = id)?.asExternal()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun delete(id: Long): Boolean {
        return try {
            val rowsAffected = productDao.delete(id = id)

            if (rowsAffected <= 0) return false

            remoteCallWithRetry(
                call = { productRemoteData.deleteById(id = id) },
                error = {
                    // TODO: notify out of sync?
                },
            )

            return true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getAll(): Flow<List<Product>> {
        return try {
            productDao.getAll().map { it.map { product -> product.asExternal() } }
        } catch (e: Exception) {
            emptyFlow()
        }
    }
}
