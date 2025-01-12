package com.bruno13palhano.mais1venda.repository

import com.bruno13palhano.data.repository.CompanyRepository

internal class TestCompanyRepository(
    private val shouldReturnError: Boolean = false,
) : CompanyRepository {
    override suspend fun authenticate(
        email: String,
        password: String,
    ): Boolean {
        return !shouldReturnError
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: String,
    ): Boolean {
        return !shouldReturnError
    }
}
