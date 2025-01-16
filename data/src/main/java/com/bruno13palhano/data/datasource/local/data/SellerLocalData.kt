package com.bruno13palhano.data.datasource.local.data

import com.bruno13palhano.data.model.company.Seller
import kotlinx.coroutines.flow.Flow

interface SellerLocalData {
    suspend fun insert(seller: Seller)

    suspend fun update(seller: Seller)

    suspend fun delete(id: Long)

    suspend fun getAll(): Flow<List<Seller>>
}
