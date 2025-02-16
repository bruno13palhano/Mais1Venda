package com.bruno13palhano.data.datasource.remote.service

import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.customer.Customer
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

    @POST("product/insert")
    suspend fun insertProduct(@Body product: Product): Response<Boolean>

    @POST("customers/insert")
    suspend fun insertCustomer(@Body customer: Customer): Response<Boolean>

    @POST("customers/customer/{id}")
    suspend fun getCustomerByUid(@Path("uid") uid: String): Response<Customer?>

    @POST("customers/all")
    suspend fun getAllCustomers(): Response<List<Customer>>

    @POST("product/update")
    suspend fun updateProduct(@Body product: Product): Response<Boolean>

    @POST("product/delete/{id}")
    suspend fun deleteProductById(@Path("id") id: Long): Response<Boolean>

    @POST("product/all")
    suspend fun getAllProducts(): Response<List<Product>>
}
