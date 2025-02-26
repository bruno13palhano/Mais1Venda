package com.bruno13palhano.data.repository

import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Ad
import kotlinx.coroutines.flow.Flow

interface AdRepository {
    suspend fun insert(ad: Ad): Resource<Boolean>
    suspend fun update(ad: Ad): Resource<Boolean>
    suspend fun delete(id: Long): Resource<Boolean>
    suspend fun get(id: Long): Ad?
    fun getAll(): Flow<List<Ad>>
}
