package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.model.customer.Customer

class CustomerRemoteData {
    suspend fun insert(customer: Customer): Customer {
        TODO("Not yet implemented")
    }

    suspend fun getByUid(uid: String): Customer? {
        TODO("Not yet implemented")
    }

    suspend fun getAll(): List<Customer> {
        TODO("Not yet implemented")
    }
}
