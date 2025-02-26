package com.bruno13palhano.data.repository

import com.bruno13palhano.data.datasource.local.dao.AdDao
import com.bruno13palhano.data.datasource.local.dao.ProductDao
import com.bruno13palhano.data.datasource.local.entity.AdEntity
import com.bruno13palhano.data.datasource.remote.source.AdRemoteData
import com.bruno13palhano.data.model.company.asExternal
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Ad
import com.bruno13palhano.data.model.shared.asInternal
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class AdRepositoryImpl @Inject constructor(
    private val adRemoteData: AdRemoteData,
    private val adDao: AdDao,
    private val productDao: ProductDao,
) : AdRepository {
    override suspend fun insert(ad: Ad): Resource<Boolean> {
        return adRemoteData.insert(ad = ad)
            .apply {
                if (this is Resource.Success) {
                    adDao.insert(ad = ad.asInternal())
                }
            }
    }

    override suspend fun update(ad: Ad): Resource<Boolean> {
        return adRemoteData.update(ad = ad)
            .apply {
                if (this is Resource.Success) {
                    adDao.update(ad = ad.asInternal())
                }
            }
    }

    override suspend fun delete(id: Long): Resource<Boolean> {
        return adRemoteData.delete(id = id)
            .apply {
                if (this is Resource.Success) {
                    adDao.delete(id = id)
                }
            }
    }

    override suspend fun get(id: Long): Ad? {
        return adDao.getById(id = id)?.let { adEntityToAd(ad = it) }
    }

    override fun getAll(): Flow<List<Ad>> {
        return adDao.getAll().map { it.map { ad -> adEntityToAd(ad = ad) } }
    }

    private suspend fun adEntityToAd(ad: AdEntity): Ad {
        val product = productDao.getById(id = ad.productId)!!.asExternal() // handle null case

        return Ad(
            id = ad.id,
            title = ad.title,
            product = product,
            price = ad.price,
            description = ad.description,
            observations = ad.observations,
            off = ad.off,
            stockQuantity = ad.stockQuantity,
            unitsSold = ad.unitsSold,
            questions = ad.questions,
            reviews = ad.reviews,
            lastModifiedTimestamp = ad.lastModifiedTimestamp,
        )
    }
}
