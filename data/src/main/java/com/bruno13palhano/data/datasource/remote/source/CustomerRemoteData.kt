package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.resource.Resource
import javax.inject.Inject

internal class CustomerRemoteData @Inject constructor(
    private val api: ApiService,
) {
    suspend fun insert(customer: Customer): Resource<Boolean> {
        return safeApiCall { api.insertCustomer(customer = customer) }
    }

    suspend fun getByUid(uid: String): Resource<Customer?> {
        return safeApiCall { api.getCustomerByUid(uid = uid) }
    }

    suspend fun getAll(): Resource<List<Customer>> {
        return safeApiCall { api.getAllCustomers() }
    }
}
