package com.bruno13palhano.data.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.model.shared.SocialMedia

@Entity(tableName = "customer")
internal data class CustomerEntity(
    @PrimaryKey
    val uid: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address,
    val socialMedia: List<SocialMedia>,
    val orders: List<Order>,
    val lastModifiedTimestamp: String,
)
