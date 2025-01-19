package com.bruno13palhano.data.model.company

import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.SocialMedia
import kotlinx.serialization.Serializable

@Serializable
data class Seller(
    val id: Long,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address,
    val socialMedia: List<SocialMedia>,
    val sector: String,
    val lastModifiedTimestamp: String,
)
