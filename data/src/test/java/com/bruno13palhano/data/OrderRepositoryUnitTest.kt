package com.bruno13palhano.data

import com.bruno13palhano.data.datasource.local.dao.CustomerDao
import com.bruno13palhano.data.datasource.local.dao.OrderDao
import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.local.entity.OrderEntity
import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.datasource.remote.source.OrderRemoteData
import com.bruno13palhano.data.model.customer.Customer
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.model.shared.OrderStatus
import com.bruno13palhano.data.repository.OrderRepositoryImpl
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

internal class OrderRepositoryUnitTest {
    @MockK
    lateinit var mockApi: ApiService

    @MockK
    lateinit var mockOrderDao: OrderDao

    @MockK
    lateinit var mockProductDao: ProductDao

    @MockK
    lateinit var mockCustomerDao: CustomerDao

    @InjectMockKs
    lateinit var mockRemoteData: OrderRemoteData

    private lateinit var testSut: OrderRepositoryImpl

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        testSut = OrderRepositoryImpl(
            orderRemoteData = mockRemoteData,
            orderDao = mockOrderDao,
            productDao = mockProductDao,
            customerDao = mockCustomerDao,
        )
    }

    private val customer = Customer(
        uid = "1",
        name = "Customer 1",
        email = "email 1",
        phone = "phone 1",
        address = Address("", "", "", ""),
        socialMedia = emptyList(),
        orders = emptyList(),
        lastModifiedTimestamp = "",
    )

    private val order = Order(
        id = 1,
        productName = "Product 1",
        productCode = "1234567",
        quantity = 1,
        unitPrice = 100.0,
        off = 1.2f,
        totalPrice = 100.0,
        customer = customer,
        orderDate = 111111111111212,
        deliveryDate = 12234523453245,
        status = OrderStatus.PROCESSING_ORDER,
        lastModifiedTimestamp = "",
    )

    @Test
    fun `successful get order should return the Order`() = runTest {
        coEvery { mockOrderDao.insert(order = orderToEntity(order)) } returns 1
        coEvery { mockApi.confirmOrder(id = order.id) } returns Response.success(true)

        val result = testSut.confirmOrder(id = order.id)

        coVerify(exactly = 1) { mockApi.confirmOrder(id = order.id) }

        assertThat(result.data).isEqualTo(true)
    }

    private fun orderToEntity(order: Order) = OrderEntity(
        id = order.id,
        productId = 1L,
        quantity = order.quantity,
        unitPrice = order.unitPrice,
        off = order.off,
        totalPrice = order.totalPrice,
        customerUid = order.customer.uid,
        orderDate = order.orderDate,
        deliveryDate = order.deliveryDate,
        status = order.status,
        lastModifiedTimestamp = order.lastModifiedTimestamp,
    )
}
