package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.resource.Resource
import javax.inject.Inject

internal class CompanyRemoteData @Inject constructor(
    private val api: ApiService,
) {
    suspend fun createCompany(company: Company): Resource<Company> {
        return safeApiCall { api.createCompany(company = company) }
    }

    suspend fun login(email: String, password: String): Resource<Boolean> {
        return safeApiCall { api.login(email = email, password = password) }
    }
}
