package com.bruno13palhano.data.repository

interface CompanyRepository {
    suspend fun authenticate(email: String, password: String): Boolean

    suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: String,
    ): Boolean
}
