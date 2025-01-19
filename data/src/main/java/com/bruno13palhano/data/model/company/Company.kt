package com.bruno13palhano.data.model.company

import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.SocialMedia

data class Company(
    val uid: String,
    val name: String,
    val email: String,
    val password: String,
    val phone: String,
    val address: Address,
    val sellers: List<Seller>,
    val socialMedia: List<SocialMedia>,
    val lastModifiedTimestamp: String,
)
