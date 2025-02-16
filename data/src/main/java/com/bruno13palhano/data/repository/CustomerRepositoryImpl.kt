package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.CustomerDao
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.customer.asExternal
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import javax.inject.Inject

internal class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao,
) : CustomerRepository {
    override suspend fun get(uid: String): Resource<Customer?> {
        return try {
            val result = customerDao.getById(uid = uid)?.asExternal()

            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }

    override suspend fun getAll(): Resource<List<Customer>> {
        return try {
            val result = customerDao.getAll().map { it.asExternal() }

            Resource.Success(result)
        } catch (e: Exception) {
            Resource.Error(errorType = ErrorType.UNKNOWN)
        }
    }
}
