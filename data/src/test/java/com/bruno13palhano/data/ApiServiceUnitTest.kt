package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.shared.Address
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal class ApiServiceUnitTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val companyAdapter = moshi.adapter(Company::class.java)

    private val retrofit = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
    private val testApi: ApiService by lazy { retrofit.create(ApiService::class.java) }

    @Test
    fun `verify login path`() = runTest {
        val email = "company@email.com"
        val password = "12345678"
        val expected = "/login/$email/$password"

        mockWebServer.enqueue(
            MockResponse()
                .setBody(true.toString())
                .setResponseCode(200),
        )

        testApi.login(email = email, password = password)

        val request = mockWebServer.takeRequest()

        assertEquals(expected, request.path)
    }

    @Test
    fun `verify create path`() = runTest {
        val company = Company(
            uid = "",
            name = "",
            email = "",
            password = "",
            address = Address("", "", "", ""),
            phone = "",
            sellers = emptyList(),
            socialMedia = emptyList(),
            lastModifiedTimestamp = "",
        )
        val companyJSON: String = companyAdapter.toJson(company)
        val expected = "/create/account"

        mockWebServer.enqueue(
            MockResponse()
                .setBody(companyJSON)
                .setResponseCode(200),
        )

        testApi.createCompany(company = company)

        val request = mockWebServer.takeRequest()

        assertEquals(expected, request.path)
    }
}
