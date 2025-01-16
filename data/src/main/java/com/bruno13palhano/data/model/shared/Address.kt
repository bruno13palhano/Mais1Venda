package com.bruno13palhano.data.model.shared

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val street: String,
    val number: String,
    val complement: String,
    val city: String,
)
