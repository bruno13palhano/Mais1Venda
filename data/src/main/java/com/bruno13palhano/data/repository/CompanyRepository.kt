package com.bruno13palhano.data.repository

interface CompanyRepository {
    suspend fun authenticate(
        email: String,
        password: String,
    ): Boolean
}
