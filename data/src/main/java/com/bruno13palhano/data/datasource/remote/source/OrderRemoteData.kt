package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Order
import javax.inject.Inject

internal class OrderRemoteData @Inject constructor(private val api: ApiService) {
    suspend fun getAll(): Resource<List<Order>> {
        return safeApiCall { api.getAllOrders() }
    }

    suspend fun getNewOrders(): Resource<List<Order>> {
        return safeApiCall { api.getAllNewOrders() }
    }

    suspend fun confirmOrder(id: Long): Resource<Boolean> {
        return safeApiCall { api.confirmOrder(id = id) }
    }

    suspend fun cancelOrder(id: Long): Resource<Boolean> {
        return safeApiCall { api.cancelOrder(id = id) }
    }
}
