package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.local.dao.CompanyDao
import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.datasource.remote.source.CompanyRemoteData
import com.bruno13palhano.data.model.company.Company
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.RemoteResponseError
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.repository.CompanyRepositoryImpl
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.mockk.MockKAnnotations
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

internal class CompanyRepositoryUnitTest {
    @get:Rule
    val mockWebServer = MockWebServer()

    @MockK
    lateinit var mockApi: ApiService

    @MockK
    lateinit var mockDao: CompanyDao

    @InjectMockKs
    lateinit var mockRemoteData: CompanyRemoteData

    @InjectMockKs
    lateinit var testSut: CompanyRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    private val expectedCompany = Company(
        uid = "",
        name = "company",
        email = "email",
        password = "password",
        phone = "phone",
        address = Address("", "", "", ""),
        sellers = emptyList(),
        socialMedia = emptyList(),
        lastModifiedTimestamp = getCurrentTimestamp(),
    )

    @Test
    fun `successful createAccount should returned the new created company`() = runTest {
        coEvery { mockApi.createCompany(expectedCompany) } returns Response.success(expectedCompany)
        coEvery { mockDao.insert(any()) } returns Unit

        val result: Resource<Company> = testSut.createAccount(
            email = expectedCompany.email,
            password = expectedCompany.password,
            companyName = expectedCompany.name,
            phone = expectedCompany.phone,
            address = expectedCompany.address,
        )

        coVerify(exactly = 1) { mockApi.createCompany(expectedCompany) }
        coVerify(exactly = 1) { mockDao.insert(expectedCompany.asInternal()) }

        assertThat(result.data).isEqualTo(expectedCompany)
    }

    @Test
    fun `successful createAccount with empty body should return RemoteResponseError`() = runTest {
        val expected = RemoteResponseError(code = "500", description = "Server Error")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(RemoteResponseError::class.java)
        val expectedJson = adapter.toJson(expected)

        coEvery {
            mockApi.createCompany(expectedCompany)
        } returns
            Response.error(
                500,
                expectedJson.toResponseBody("application/json".toMediaType()),
            )

        val result: Resource<Company> = testSut.createAccount(
            email = expectedCompany.email,
            password = expectedCompany.password,
            companyName = expectedCompany.name,
            phone = expectedCompany.phone,
            address = expectedCompany.address,
        )

        coVerify(exactly = 1) { mockApi.createCompany(expectedCompany) }

        assertThat(result.remoteResponseError).isEqualTo(expected)
    }

    @Test
    fun `failure createAccount should return SERVER ErrorType`() = runTest {
        coEvery { mockApi.createCompany(expectedCompany) }.throws(
            HttpException(
                Response.error<Any>(500, "".toResponseBody("plain/text".toMediaType())),
            ),
        )

        val result: Resource<Company> = testSut.createAccount(
            email = expectedCompany.email,
            password = expectedCompany.password,
            companyName = expectedCompany.name,
            phone = expectedCompany.phone,
            address = expectedCompany.address,
        )

        coVerify(exactly = 1) { mockApi.createCompany(expectedCompany) }
        verify { mockDao wasNot called }

        assertThat(result.errorType).isEqualTo(ErrorType.SERVER)
    }

    @Test
    fun `createAccount without connection should return NO_INTERNET ErrorType`() = runTest {
        coEvery { mockApi.createCompany(expectedCompany) }.throws(IOException())

        val result: Resource<Company> = testSut.createAccount(
            email = expectedCompany.email,
            password = expectedCompany.password,
            companyName = expectedCompany.name,
            phone = expectedCompany.phone,
            address = expectedCompany.address,
        )

        coVerify(exactly = 1) { mockApi.createCompany(expectedCompany) }
        verify { mockDao wasNot called }

        assertThat(result.errorType).isEqualTo(ErrorType.NO_INTERNET)
    }

    @Test
    fun `createAccount with other exception should return UNKNOWN ErrorType`() = runTest {
        coEvery { mockApi.createCompany(expectedCompany) }.throws(Exception())

        val result: Resource<Company> = testSut.createAccount(
            email = expectedCompany.email,
            password = expectedCompany.password,
            companyName = expectedCompany.name,
            phone = expectedCompany.phone,
            address = expectedCompany.address,
        )

        coVerify(exactly = 1) { mockApi.createCompany(expectedCompany) }
        verify { mockDao wasNot called }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }

    @Test
    fun `successful login should return true`() = runTest {
        coEvery {
            mockApi.login(expectedCompany.email, expectedCompany.password)
        } returns Response.success(true)

        val result: Resource<Boolean> = testSut.login(
            email = expectedCompany.email,
            password = expectedCompany.password,
        )

        coVerify(exactly = 1) { mockApi.login(expectedCompany.email, expectedCompany.password) }

        assertThat(result.data).isEqualTo(true)
    }

    @Test
    fun `failure login should return false`() = runTest {
        val expected = RemoteResponseError(code = "500", description = "Server Error")

        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter = moshi.adapter(RemoteResponseError::class.java)
        val expectedJson = adapter.toJson(expected)

        coEvery {
            mockApi.login(expectedCompany.email, expectedCompany.password)
        } returns
            Response.error(
                500,
                expectedJson.toResponseBody("application/json".toMediaType()),
            )

        val result: Resource<Boolean> = testSut.login(
            email = expectedCompany.email,
            password = expectedCompany.password,
        )

        coVerify(exactly = 1) { mockApi.login(expectedCompany.email, expectedCompany.password) }

        assertThat(result.remoteResponseError).isEqualTo(expected)
    }

    @Test
    fun `failure login should return SERVER ErrorType`() = runTest {
        coEvery {
            mockApi.login(expectedCompany.email, expectedCompany.password)
        }.throws(HttpException(Response.error<Any>(500, "".toResponseBody("plain/text".toMediaType()))))

        val result: Resource<Boolean> = testSut.login(
            email = expectedCompany.email,
            password = expectedCompany.password,
        )

        coVerify(exactly = 1) { mockApi.login(expectedCompany.email, expectedCompany.password) }

        assertThat(result.errorType).isEqualTo(ErrorType.SERVER)
    }

    @Test
    fun `login without connection should return NO_INTERNET ErrorType`() = runTest {
        coEvery { mockApi.login(expectedCompany.email, expectedCompany.password) }.throws(IOException())

        val result: Resource<Boolean> = testSut.login(
            email = expectedCompany.email,
            password = expectedCompany.password,
        )

        coVerify(exactly = 1) { mockApi.login(expectedCompany.email, expectedCompany.password) }

        assertThat(result.errorType).isEqualTo(ErrorType.NO_INTERNET)
    }

    @Test
    fun `login with other exception should return UNKNOWN ErrorType`() = runTest {
        coEvery { mockApi.login(expectedCompany.email, expectedCompany.password) }.throws(Exception())

        val result: Resource<Boolean> = testSut.login(
            email = expectedCompany.email,
            password = expectedCompany.password,
        )

        coVerify(exactly = 1) { mockApi.login(expectedCompany.email, expectedCompany.password) }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }

    private fun getCurrentTimestamp(): String {
        return DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
        ).format(OffsetDateTime.now(ZoneOffset.UTC))
    }
}
