package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.model.company.Company

class CompanyRemoteData {
    suspend fun createCompany(company: Company): Company {
        TODO("Not yet implemented")
    }

    suspend fun login(email: String, password: String): Boolean {
        TODO("Not yet implemented")
    }
}
