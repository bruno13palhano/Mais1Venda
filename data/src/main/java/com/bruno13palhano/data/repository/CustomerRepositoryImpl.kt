package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.CustomerDao
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.customer.asExternal
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

internal class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao,
) : CustomerRepository {
    override suspend fun get(uid: String): Customer? {
        return try {
            customerDao.getById(uid = uid)?.asExternal()
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAll(): Flow<List<Customer>> {
        return try {
            customerDao.getAll().map { it.map { customer -> customer.asExternal() } }
        } catch (e: Exception) {
            emptyFlow()
        }
    }
}
