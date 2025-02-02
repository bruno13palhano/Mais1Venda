package com.bruno13palhano.data.repository.shared

import com.bruno13palhano.data.model.resource.Resource

/**
 * @param retries: Number of retries
 * @param call: The initial api call to be called
 * @param success: Callback to be called when the api call is successful
 * @param retry: Callback to be retried if the api call fails
 */
internal suspend fun <T> remoteCallWithRetry(
    retries: Int = 3,
    call: suspend () -> Resource<T>,
    success: (response: T?) -> Unit,
    retry: suspend () -> Unit,
) {
    val resource = call()
    var remainingTries = retries

    while (resource.data == null && remainingTries > 0) {
        remainingTries--
        retry()
    }

    if (resource.data != null) success(resource.data)
}
