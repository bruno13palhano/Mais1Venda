package com.bruno13palhano.data.model.customer

import com.bruno13palhano.data.datasource.local.entity.CustomerEntity
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
    val lastModifiedTimestamp: String,
)

internal fun Customer.asInternal() = CustomerEntity(
    uid = uid,
    name = name,
    email = email,
    phone = phone,
    address = address,
    socialMedia = socialMedia,
    orders = orders,
    lastModifiedTimestamp = lastModifiedTimestamp,
)

internal fun CustomerEntity.asExternal() = Customer(
    uid = uid,
    name = name,
    email = email,
    phone = phone,
    address = address,
    socialMedia = socialMedia,
    orders = orders,
    lastModifiedTimestamp = lastModifiedTimestamp,
)
