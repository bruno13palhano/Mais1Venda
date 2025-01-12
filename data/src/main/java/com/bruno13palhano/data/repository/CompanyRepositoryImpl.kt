package com.bruno13palhano.data.repository

import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.delay

internal class CompanyRepositoryImpl @Inject constructor() : CompanyRepository {
    override suspend fun authenticate(email: String, password: String): Boolean {
        delay(3000)
        return Random.nextBoolean()
    }

    override suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: String,
    ): Boolean {
        delay(3000)
        return Random.nextBoolean()
    }
}
