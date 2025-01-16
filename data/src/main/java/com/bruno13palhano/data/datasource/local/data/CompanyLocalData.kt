package com.bruno13palhano.data.datasource.local.data

import com.bruno13palhano.data.model.company.Company

interface CompanyLocalData {
    suspend fun insert(company: Company)

    suspend fun update(company: Company)

    suspend fun delete(uid: String)

    suspend fun getCompany(uid: String): Company?
}
