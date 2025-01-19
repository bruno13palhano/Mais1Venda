package com.bruno13palhano.data.datasource.remote.service

import com.bruno13palhano.data.model.company.Company
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

internal interface ApiService {
    @POST("create/account")
    suspend fun createCompany(@Body company: Company): Response<Company>

    @POST("login/{email}/{password}")
    suspend fun login(
        @Path("email") email: String,
        @Path("password") password: String,
    ): Response<Boolean>
}
