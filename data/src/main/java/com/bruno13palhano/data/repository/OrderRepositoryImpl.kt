package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.CustomerDao
import com.bruno13palhano.data.datasource.local.dao.OrderDao
import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.local.entity.OrderEntity
import com.bruno13palhano.data.datasource.remote.source.OrderRemoteData
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.customer.asExternal
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.repository.shared.remoteCallWithRetry
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class OrderRepositoryImpl @Inject constructor(
    private val orderRemoteData: OrderRemoteData,
    private val orderDao: OrderDao,
    private val productDao: ProductDao,
    private val customerDao: CustomerDao,
) : OrderRepository {
    override suspend fun update(order: Order) {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Long): Order? {
        val order = orderDao.getById(id = id)

        return if (order != null) orderEntityToOrder(order = order) else null
    }

    override fun getAll(): Flow<List<Order>> {
        return orderDao.getAll().map { ordersEntityToOrders(orders = it) }
    }

    override suspend fun getNewOrders(): Resource<List<Order>> {
        return orderRemoteData.getNewOrders()
    }

    override suspend fun confirmOrder(id: Long): Resource<Boolean> {
        return remoteCallWithRetry { orderRemoteData.confirmOrder(id = id) }
    }

    override suspend fun cancelOrder(id: Long): Resource<Boolean> {
        return remoteCallWithRetry { orderRemoteData.cancelOrder(id = id) }
    }

    private suspend fun ordersEntityToOrders(orders: List<OrderEntity>): List<Order> {
        return orders.map { order -> orderEntityToOrder(order = order) }
    }

    private suspend fun orderEntityToOrder(order: OrderEntity): Order {
        return Order(
            id = order.id,
            product = productDao.getById(id = order.productId)!!.asExternal(),
            customer = customerDao.getById(uid = order.customerUid)!!.asExternal(),
            orderDate = order.orderDate,
            deliveryDate = order.deliveryDate,
            status = order.status,
            lastModifiedTimestamp = order.lastModifiedTimestamp,
        )
    }
}
