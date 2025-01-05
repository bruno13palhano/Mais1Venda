package com.bruno13palhano.data.model.company

import com.bruno13palhano.data.model.shared.Address
import com.bruno13palhano.data.model.shared.SocialMedia

data class Seller(
    val uid: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address,
    val socialMedia: List<SocialMedia>,
    val sector: String,
    val lastModifiedTimestamp: Long,
)
