package com.bruno13palhano.data.datasource.remote.service

import com.bruno13palhano.data.model.company.Company
import retrofit2.Response
import retrofit2.http.POST

internal interface ServiceImpl : Service {
    @POST("create/account")
    override suspend fun createCompany(company: Company): Response<Company>

    @POST("login")
    override suspend fun login(email: String, password: String): Response<Boolean>
}
