package com.bruno13palhano.data.model.customer

import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.Order
import com.bruno13palhano.data.model.shared.SocialMedia

data class Customer(
    val uid: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address,
    val socialMedia: List<SocialMedia>,
    val orders: List<Order>,
    val lastModifiedTimestamp: Long,
)
