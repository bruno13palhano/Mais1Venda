package com.bruno13palhano.data.datasource.local.data

interface CompanyLocalData<T> {
    suspend fun insert(company: T)

    suspend fun update(company: T)

    suspend fun delete(uid: String)

    suspend fun getCompany(uid: String): T?
}
