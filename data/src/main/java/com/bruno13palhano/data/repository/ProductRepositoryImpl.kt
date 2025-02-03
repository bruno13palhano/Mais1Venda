package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.log.AppLog
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.repository.shared.remoteCallWithRetry
import javax.inject.Inject

private const val REMOTE_TAG = "ProductRepository-Remote"
private const val LOCAL_TAG = "ProductRepository-Local"

internal class ProductRepositoryImpl @Inject constructor(
    private val productRemoteData: ProductRemoteData,
    private val productDao: ProductDao,
    private val log: AppLog,
) : ProductRepository {
    override suspend fun insert(product: Product): Resource<Boolean> {
        return try {
            val id = productDao.insert(product = product.asInternal())

            if (id <= 0) {
                log.logInfo(tag = LOCAL_TAG, message = "Product not inserted")
                return Resource.Success(false)
            }
            log.logInfo(tag = LOCAL_TAG, message = "Product inserted successfully")

            remoteCallWithRetry(
                call = { productRemoteData.insert(product.copy(id = id)) },
                success = { it?.let { success -> logRemoteProductInsertion(success = success) } },
            )

            Resource.Success(data = true)
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: ErrorType.UNKNOWN.name)
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun update(product: Product): Resource<Boolean> {
        return try {
            val rowsAffected = productDao.update(product = product.asInternal())

            if (rowsAffected <= 0) {
                log.logInfo(tag = LOCAL_TAG, message = "Product not updated")
                return Resource.Success(false)
            }
            log.logInfo(tag = LOCAL_TAG, message = "Product updated successfully")

            remoteCallWithRetry(
                call = { productRemoteData.update(product = product) },
                success = { it?.let { success -> logRemoteProductUpdate(success = success) } },
            )

            Resource.Success(data = true)
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: ErrorType.UNKNOWN.name)
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun delete(id: Long): Resource<Boolean> {
        return try {
            val rowsAffected = productDao.delete(id = id)

            if (rowsAffected <= 0) {
                log.logInfo(tag = LOCAL_TAG, message = "Product not deleted")
                return Resource.Success(false)
            }
            log.logInfo(tag = LOCAL_TAG, message = "Product deleted successfully")

            remoteCallWithRetry(
                call = { productRemoteData.deleteById(id = id) },
                success = { it?.let { success -> logRemoteProductDeletion(success = success) } },
            )

            return Resource.Success(true)
        } catch (e: Exception) {
            log.logError(tag = LOCAL_TAG, message = e.message ?: ErrorType.UNKNOWN.name)
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun getAll(): Resource<List<Product>> {
        var logMessage = "Products retrieved successfully"

        return try {
            val products = productDao.getAll().map { it.asExternal() }
            Resource.Success(data = products)
        } catch (e: Exception) {
            logMessage = e.message ?: ErrorType.UNKNOWN.name
            Resource.Error(errorType = ErrorType.UNKNOWN)
        } finally {
            log.logInfo(tag = LOCAL_TAG, message = logMessage)
        }
    }

    private fun logRemoteProductInsertion(success: Boolean) {
        var message = "Product inserted successfully"

        if (!success) {
            message = "Product not inserted"
        }

        log.logInfo(tag = REMOTE_TAG, message = message)
    }

    private fun logRemoteProductUpdate(success: Boolean) {
        var message = "Product updated successfully"

        if (!success) {
            message = "Product not updated"
        }

        log.logInfo(tag = REMOTE_TAG, message = message)
    }

    private fun logRemoteProductDeletion(success: Boolean) {
        var message = "Product deleted successfully"

        if (!success) {
            message = "Product not deleted"
        }

        log.logInfo(tag = REMOTE_TAG, message = message)
    }
}
