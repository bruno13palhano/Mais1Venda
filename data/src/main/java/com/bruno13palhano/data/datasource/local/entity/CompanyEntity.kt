package com.bruno13palhano.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.data.model.company.Seller
import com.bruno13palhano.data.model.shared.SocialMedia
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "company")
data class CompanyEntity(
    @PrimaryKey
    val uid: String,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: String,
    val sellers: List<Seller>,
    val socialMedia: List<SocialMedia>,
    val lastModifiedTimestamp: Long,
)
