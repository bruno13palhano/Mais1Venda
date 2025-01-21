package com.bruno13palhano.data.model.resource

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RemoteResponseError(
    @Json(name = "code") val code: String?,
    @Json(name = "message") val description: String?,
)
