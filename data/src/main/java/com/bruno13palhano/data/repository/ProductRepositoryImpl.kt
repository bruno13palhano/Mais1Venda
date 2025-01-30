package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.log.AppLog
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import javax.inject.Inject

private const val SERVER_TAG = "ProductRepository-Remote"

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteData: ProductRemoteData,
    private val productDao: ProductDao,
    private val log: AppLog
) : ProductRepository {
    override suspend fun insert(product: Product): Resource<Boolean> {
        return try {
            val id = productDao.insert(product = product.asInternal())
            val response = productRemoteData.insert(product.copy(id = id))

            response.data?.let { success ->
                if (success) {
                    log.logInfo(tag = SERVER_TAG, message = "Product inserted successfully")
                } else {
                    log.logInfo(tag = SERVER_TAG, message = "Product not inserted")
                }
            }

            // TODO: check if id is not zero?

            Resource.Success(data = true)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun update(product: Product): Resource<Boolean> {
        return try {
            val rowsAffected = productDao.update(product = product.asInternal())

            if (rowsAffected > 0) {
                val response = productRemoteData.update(product)
                response.data?.let { success ->
                    if (success) {
                        log.logInfo(tag = SERVER_TAG, message = "Product updated successfully")
                    } else {
                        log.logInfo(tag = SERVER_TAG, message = "Product not updated")
                    }
                }
            } else {
                // TODO: retry and log info?
            }

            Resource.Success(data = true)
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun delete(id: Long): Resource<Boolean> {
        return try {
            productDao.delete(id = id)

            val response = productRemoteData.deleteById(id = id)
            response.data?.let { success ->
                if (success) {
                    log.logInfo(tag = SERVER_TAG, message = "Product deleted successfully")
                } else {
                    log.logInfo(tag = SERVER_TAG, message = "Product not deleted")
                }
            }

            return Resource.Success(true)
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun getAll(): Resource<List<Product>> {
        return try {
            Resource.Success(productDao.getAll().map { it.asExternal() })
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }
}
