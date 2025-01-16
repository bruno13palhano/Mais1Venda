package com.bruno13palhano.data.model.shared

import kotlinx.serialization.Serializable

@Serializable
data class SocialMedia(
    val name: String,
    val value: String,
)
