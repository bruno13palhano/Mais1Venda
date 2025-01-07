package com.bruno13palhano.mais1venda.repository

import com.bruno13palhano.data.repository.CompanyRepository
import kotlin.random.Random
import kotlinx.coroutines.delay

internal class TestCompanyRepository : CompanyRepository {
    override suspend fun authenticate(email: String, password: String): Boolean {
        return true
    }
}
