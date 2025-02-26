package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.datasource.remote.service.ApiService
import com.bruno13palhano.data.model.resource.Resource
import com.bruno13palhano.data.model.shared.Ad
import javax.inject.Inject

internal class AdRemoteData @Inject constructor(private  val api: ApiService) {
    suspend fun insert(ad: Ad): Resource<Boolean> {
        return safeApiCall { api.insertAd(ad = ad) }
    }

    suspend fun update(ad: Ad): Resource<Boolean> {
        return safeApiCall { api.updateAd(ad = ad) }
    }

    suspend fun delete(id: Long): Resource<Boolean> {
        return safeApiCall { api.deleteAdById(id = id) }
    }

    suspend fun getAll(): Resource<List<Ad>> {
        return safeApiCall { api.getAllAds() }
    }
}
