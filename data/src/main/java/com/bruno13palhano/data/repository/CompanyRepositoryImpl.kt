package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.CompanyDao
import com.bruno13palhano.data.datasource.local.entity.CompanyEntity
import com.bruno13palhano.data.datasource.remote.source.CompanyRemoteData
import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Address
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class CompanyRepositoryImpl @Inject constructor(
    private val companyRemoteData: CompanyRemoteData,
    private val companyDao: CompanyDao,
) : CompanyRepository {
    override suspend fun login(email: String, password: String): Resource<Boolean> {
        return companyRemoteData.login(email = email, password = password)
    }

    // TODO: change implementation to return just the uid?
    override suspend fun createAccount(
        email: String,
        password: String,
        companyName: String,
        phone: String,
        address: Address,
    ): Resource<Company> {
        return companyRemoteData.createCompany(
            company = Company(
                uid = "",
                email = email,
                password = password,
                name = companyName,
                phone = phone,
                address = address,
                sellers = emptyList(),
                socialMedia = emptyList(),
                lastModifiedTimestamp = getCurrentTimestamp(),
            ),
        ).apply {
            if (this is Resource.Success) {
                data?.let {
                    companyDao.insert(
                        company = CompanyEntity(
                            uid = it.uid,
                            name = it.name,
                            email = it.email,
                            password = it.password,
                            phone = it.phone,
                            address = it.address,
                            sellers = it.sellers,
                            socialMedia = it.socialMedia,
                            lastModifiedTimestamp = it.lastModifiedTimestamp,
                        ),
                    )
                }
            }
        }
    }

    private fun getCurrentTimestamp(): String =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .format(OffsetDateTime.now(ZoneOffset.UTC))
}
