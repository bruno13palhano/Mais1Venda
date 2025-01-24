package com.bruno13palhano.mais1venda.repository

import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.repository.CompanyRepository

internal class TestCompanyRepository(
    private val shouldReturnError: Boolean = false,
) : CompanyRepository {
    override suspend fun login(email: String, password: String): Resource<Boolean> {
        return if (!shouldReturnError) {
            Resource.Success(data = true)
        } else {
            Resource.Error(errorType = ErrorType.SERVER)
        }
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: Address,
    ): Resource<Company> {
        return if (!shouldReturnError) {
            Resource.Success(
                data = Company(
                    uid = "",
                    email = email,
                    password = password,
                    name = companyName,
                    phone = phone,
                    address = address,
                    sellers = emptyList(),
                    socialMedia = emptyList(),
                    lastModifiedTimestamp = "",
                ),
            )
        } else {
            Resource.Error(errorType = ErrorType.SERVER)
        }
    }
}
