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
private const val LOCAL_TAG = "ProductRepository-Local"

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteData: ProductRemoteData,
    private val productDao: ProductDao,
    private val log: AppLog
) : ProductRepository {
    override suspend fun insert(product: Product): Resource<Boolean> {
        return try {
            val id = productDao.insert(product = product.asInternal())
            val response = productRemoteData.insert(product.copy(id = id))

            if (id > 0) {
                response.data?.let { success ->
                    if (success) {
                        log.logInfo(tag = SERVER_TAG, message = "Product inserted successfully")
                    } else {
                        log.logInfo(tag = SERVER_TAG, message = "Product not inserted")
                    }
                }

                log.logInfo(tag = LOCAL_TAG, message = "Product inserted successfully")

                Resource.Success(data = true)
            } else {
                log.logInfo(tag = LOCAL_TAG, message = "Product not inserted")

                Resource.Success(data = false)
            }
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: "Unknown error")

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

                log.logInfo(tag = LOCAL_TAG, message = "Product updated successfully")

                Resource.Success(data = true)
            } else {
                log.logInfo(tag = LOCAL_TAG, message = "Product not updated")

                Resource.Success(data = false)
            }
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: "Unknown error")

            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun delete(id: Long): Resource<Boolean> {
        return try {
            val rowsAffected = productDao.delete(id = id)

            if (rowsAffected > 0) {
                val response = productRemoteData.deleteById(id = id)
                response.data?.let { success ->
                    if (success) {
                        log.logInfo(tag = SERVER_TAG, message = "Product deleted successfully")
                    } else {
                        log.logInfo(tag = SERVER_TAG, message = "Product not deleted")
                    }
                }

                log.logInfo(tag = LOCAL_TAG, message = "Product deleted successfully")

                return Resource.Success(true)
            } else {
                log.logInfo(tag = LOCAL_TAG, message = "Product not deleted")

                return Resource.Success(false)
            }
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun getAll(): Resource<List<Product>> {
        return try {
            val products = productDao.getAll().map { it.asExternal() }

            log.logInfo(tag = LOCAL_TAG, message = "Products retrieved successfully")

            Resource.Success(data = products)
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: "Unknown error")

            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }
}
