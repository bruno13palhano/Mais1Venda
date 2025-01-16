package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.company.Seller
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

internal class SellerRepositoryImpl @Inject constructor() : SellerRepository {
    override suspend fun insert(seller: Seller) {
        TODO("Not yet implemented")
    }

    override suspend fun update(seller: Seller) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): Flow<List<Seller>> {
        TODO("Not yet implemented")
    }
}
