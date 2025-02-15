package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Address

interface CompanyRepository {
    suspend fun login(email: String, password: String): Resource<Boolean>
    suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: Address,
    ): Resource<Company>
}
