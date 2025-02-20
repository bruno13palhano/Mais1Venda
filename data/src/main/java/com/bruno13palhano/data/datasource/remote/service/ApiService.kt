package com.bruno13palhano.data.datasource.remote.service

import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.shared.Order
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("customers/customer/{id}")
    suspend fun getCustomerByUid(@Path("uid") uid: String): Response<Customer?>

    @GET("customers/all")
    suspend fun getAllCustomers(): Response<List<Customer>>

    @POST("product/update")
    suspend fun updateProduct(@Body product: Product): Response<Boolean>

    @POST("product/delete/{id}")
    suspend fun deleteProductById(@Path("id") id: Long): Response<Boolean>

    @POST("product/all")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("orders/new/all")
    suspend fun getAllNewOrders(): Response<List<Order>>

    @POST("orders/confirm/{id}")
    suspend fun confirmOrder(@Path("id") id: Long): Response<Boolean>

    @POST("orders/cancel/{id}")
    suspend fun cancelOrder(@Path("id") id: Long): Response<Boolean>

    @GET("orders/all")
    suspend fun getAllOrders(): Response<List<Order>>
}
