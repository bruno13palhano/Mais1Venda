package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.datasource.remote.source.ProductRemoteData
import com.bruno13palhano.data.model.company.Product
import com.bruno13palhano.data.model.company.asInternal
import com.bruno13palhano.data.repository.ProductRepositoryImpl
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.advanceUntilIdle
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
        )
    }

    private val expectedProduct = Product(
        id = 0L,
        name = "product",
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

        val result: Boolean = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 1) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `insertion failure should return false`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) } returns 0
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }
            .returns(Response.success(true))

        val result: Boolean = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 0) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `database insert failure should return false`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) }.throws(Exception())
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        val expected = false
        val result: Boolean = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        coVerify(exactly = 0) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `remote insertion failure should retry`() = runTest {
        coEvery { mockDao.insert(expectedProduct.asInternal()) } returns 1
        coEvery { mockApi.insertProduct(expectedProduct.copy(id = 1)) }
            .throwsMany(listOf(Exception()))
            .andThen(Response.success(true))

        val result: Boolean = testSut.insert(product = expectedProduct)

        coVerify(exactly = 1) { mockDao.insert(expectedProduct.asInternal()) }
        // 2 = 1 original + 1 retry
        coVerify(exactly = 2) { mockApi.insertProduct(expectedProduct.copy(id = 1)) }

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `successful update should return true`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) } returns 1
        coEvery { mockApi.updateProduct(expected) }.returns(Response.success(true))

        val result: Boolean = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        coVerify(exactly = 1) { mockApi.updateProduct(expected) }

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `update failure should return false`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) } returns 0
        coEvery { mockApi.updateProduct(expected) }.returns(Response.success(true))

        val result: Boolean = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        coVerify(exactly = 0) { mockApi.updateProduct(expected) }

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `database update failure with exception should return false`() = runTest {
        val product = expectedProduct.copy(id = 1, name = "updated")
        val expected = false

        coEvery { mockDao.update(product.asInternal()) }.throws(Exception())
        coEvery { mockApi.updateProduct(product) }

        val result: Boolean = testSut.update(product = product)

        coVerify(exactly = 1) { mockDao.update(product.asInternal()) }
        coVerify(exactly = 0) { mockApi.updateProduct(product) }

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `remote update failure should retry`() = runTest {
        val expected = expectedProduct.copy(id = 1, name = "updated")

        coEvery { mockDao.update(expected.asInternal()) } returns 1
        coEvery { mockApi.updateProduct(expected) }
            .throwsMany(listOf(Exception(), Exception()))
            .andThen(Response.success(true))

        val result: Boolean = testSut.update(product = expected)

        coVerify(exactly = 1) { mockDao.update(expected.asInternal()) }
        // 3 = 1 original + 2 retries
        coVerify(exactly = 3) { mockApi.updateProduct(expected) }

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `successful delete should return true`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) } returns 1
        coEvery { mockApi.deleteProductById(id) } returns Response.success(true)

        val result: Boolean = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 1) { mockApi.deleteProductById(id) }

        assertThat(result).isEqualTo(true)
    }

    @Test
    fun `delete failure should return false`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) } returns 0
        coEvery { mockApi.deleteProductById(id) } returns Response.success(true)

        val result: Boolean = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 0) { mockApi.deleteProductById(id) }

        assertThat(result).isEqualTo(false)
    }

    @Test
    fun `database delete failure with exception should return false`() = runTest {
        val id = 1L
        val expected = false

        coEvery { mockDao.delete(id) }.throws(Exception())
        coEvery { mockApi.deleteProductById(id) }

        val result: Boolean = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        coVerify(exactly = 0) { mockApi.deleteProductById(id) }

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `remote delete failure should retry`() = runTest {
        val id = 1L

        coEvery { mockDao.delete(id) } returns 1
        coEvery { mockApi.deleteProductById(id) }
            .throwsMany(listOf(Exception(), Exception(), Exception()))
            .andThen(Response.success(true))

        val result: Boolean = testSut.delete(id = id)

        coVerify(exactly = 1) { mockDao.delete(id) }
        // 4 = 1 original + 3 retries
        coVerify(exactly = 4) { mockApi.deleteProductById(id) }

        assertThat(result).isEqualTo(true)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getAll should return a list of products`() = runTest {
        val expected = listOf(expectedProduct.copy(id = 1), expectedProduct.copy(id = 2))
        val products = flowOf(expected)

        coEvery { mockDao.getAll() }.returns(
            returnValue = products.map { it.map { product -> product.asInternal() } },
        )

        val result = testSut.getAll().first()
        advanceUntilIdle()

        coVerify(exactly = 1) { mockDao.getAll() }

        assertThat(result).isEqualTo(expected)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `database getAll failure with exception should return empty list`() = runTest {
        coEvery { mockDao.getAll() }.throws(Exception())

        val result = testSut.getAll()

        advanceUntilIdle()

        coVerify(exactly = 1) { mockDao.getAll() }

        assertThat(emptyFlow<List<Product>>()).isEqualTo(result)
    }
}
