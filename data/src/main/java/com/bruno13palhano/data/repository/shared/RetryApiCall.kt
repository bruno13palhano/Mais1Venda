package com.bruno13palhano.data.repository.shared

import com.bruno13palhano.data.model.resource.Resource

internal suspend fun <T> retryApiCall(
    tries: Int = 3,
    call: suspend () -> Resource<T>,
    success: (response: T?) -> Unit,
    retry: suspend () -> Unit,
) {
    val resource = call()
    var remainingTries = tries

    while (resource.data == null && remainingTries > 0) {
        remainingTries--
        retry()
    }

    if (resource.data != null) success(resource.data)
}
