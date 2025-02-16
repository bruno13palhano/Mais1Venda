package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.customer.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    suspend fun get(uid: String): Customer?
    fun getAll(): Flow<List<Customer>>
}
