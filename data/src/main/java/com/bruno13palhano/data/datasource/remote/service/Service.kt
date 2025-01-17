package com.bruno13palhano.data.datasource.remote.service

import com.bruno13palhano.data.model.company.Company
import retrofit2.Response

interface Service {
    suspend fun createCompany(company: Company): Response<Company>

    suspend fun login(email: String, password: String): Response<Boolean>
}
