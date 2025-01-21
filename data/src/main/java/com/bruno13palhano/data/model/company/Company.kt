package com.bruno13palhano.data.model.company

import com.bruno13palhano.data.datasource.local.entity.CompanyEntity
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.SocialMedia
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Company(
    @Json(name = "uid") val uid: String,
    @Json(name = "name") val name: String,
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String,
    @Json(name = "phone") val phone: String,
    @Json(name = "address") val address: Address,
    @Json(name = "sellers") val sellers: List<Seller>,
    @Json(name = "socialMedia") val socialMedia: List<SocialMedia>,
    @Json(name = "lastModifiedTimestamp") val lastModifiedTimestamp: String,
)

internal fun CompanyEntity.asExternal() = Company(
    uid = uid,
    name = name,
    email = email,
    password = password,
    phone = phone,
    address = address,
    sellers = sellers,
    socialMedia = socialMedia,
    lastModifiedTimestamp = lastModifiedTimestamp,
)

internal fun Company.asInternal() = CompanyEntity(
    uid = uid,
    name = name,
    email = email,
    password = password,
    phone = phone,
    address = address,
    sellers = sellers,
    socialMedia = socialMedia,
    lastModifiedTimestamp = lastModifiedTimestamp,
)
