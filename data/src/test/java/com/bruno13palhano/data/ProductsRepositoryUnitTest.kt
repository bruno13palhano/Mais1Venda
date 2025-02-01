package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.log.AppLog
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.repository.ProductRepositoryImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import retrofit2.Response

internal class ProductsRepositoryUnitTest {
    @MockK
    lateinit var mockApi: ApiService

    @MockK
    lateinit var mockDao: ProductDao

    @InjectMockKs
    lateinit var mockRemoteData: ProductRemoteData

    private lateinit var testSut: ProductRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        testSut = ProductRepositoryImpl(
            productRemoteData = mockRemoteData,
            productDao = mockDao,
            log = object : AppLog {
                override fun logInfo(tag: String, message: String) {
                    println("$tag: $message")
                }

                override fun logError(tag: String, message: String) {
                    println("$tag: $message")
                }
            },
        )
    }

    private val expectedProduct = Product(
        id = 0L,
        name = "product",
        price = 100f,
        category = emptyList(),
        description = "description",
        code = "1",
        quantity = 1,
        exhibitToCatalog = false,
        lastModifiedTimestamp = "",
    )

    @Test
    fun `successful insertion should return true`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) } returns 1
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }
            .returns(Response.success(true))

        val result: Resource<Boolean> = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 1) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result.data!!).isEqualTo(true)
    }

    @Test
    fun `insertion failure should return false`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) } returns 0
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }
            .returns(Response.success(true))

        val result: Resource<Boolean> = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 0) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result.data!!).isEqualTo(false)
    }

    @Test
    fun `database insert failure with exception should return ErrorType UNKNOWN`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) }.throws(Exception())
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        val result: Resource<Boolean> = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 0) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }

    @Test
    fun `successful update should return true`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) } returns 1
        coEvery { mockApi.updateProduct(expected) }.returns(Response.success(true))

        val result: Resource<Boolean> = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        coVerify(exactly = 1) { mockApi.updateProduct(expected) }

        assertThat(result.data!!).isEqualTo(true)
    }

    @Test
    fun `update failure should return false`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) } returns 0
        coEvery { mockApi.updateProduct(expected) }.returns(Response.success(true))

        val result: Resource<Boolean> = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        coVerify(exactly = 0) { mockApi.updateProduct(expected) }

        assertThat(result.data!!).isEqualTo(false)
    }

    @Test
    fun `database update failure with exception should return ErrorType UNKNOWN`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) }.throws(Exception())
        coEvery { mockApi.updateProduct(expected) }

        val result: Resource<Boolean> = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        coVerify(exactly = 0) { mockApi.updateProduct(expected) }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }

    @Test
    fun `successful delete should return true`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) } returns 1
        coEvery { mockApi.deleteProductById(id) } returns Response.success(true)

        val result: Resource<Boolean> = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 1) { mockApi.deleteProductById(id) }

        assertThat(result.data!!).isEqualTo(true)
    }

    @Test
    fun `delete failure should return false`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) } returns 0
        coEvery { mockApi.deleteProductById(id) } returns Response.success(true)

        val result: Resource<Boolean> = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 0) { mockApi.deleteProductById(id) }

        assertThat(result.data!!).isEqualTo(false)
    }

    @Test
    fun `database delete failure with exception should return ErrorType UNKNOWN`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) }.throws(Exception())
        coEvery { mockApi.deleteProductById(id) }

        val result: Resource<Boolean> = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 0) { mockApi.deleteProductById(id) }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }

    @Test
    fun `getAll should return a list of products`() = runTest {
        val expected = listOf(expectedProduct.copy(id = 1), expectedProduct.copy(id = 2))

        coEvery { mockDao.getAll() } returns expected.map { it.asInternal() }

        val result: Resource<List<Product>> = testSut.getAll()

        coVerify(exactly = 1) { mockDao.getAll() }

        assertThat(result.data).isEqualTo(expected)
    }

    @Test
    fun `database getAll failure with exception should return ErrorType UNKNOWN`() = runTest {
        coEvery { mockDao.getAll() }.throws(Exception())

        val result: Resource<List<Product>> = testSut.getAll()

        coVerify(exactly = 1) { mockDao.getAll() }

        assertThat(result.errorType).isEqualTo(ErrorType.UNKNOWN)
    }
}
