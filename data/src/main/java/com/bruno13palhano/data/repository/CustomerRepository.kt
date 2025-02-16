package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.resource.Resource

interface CustomerRepository {
    suspend fun get(uid: String): Resource<Customer?>
    suspend fun getAll(): Resource<List<Customer>>
}
