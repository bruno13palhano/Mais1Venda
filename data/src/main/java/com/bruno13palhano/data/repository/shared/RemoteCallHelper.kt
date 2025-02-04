package com.bruno13palhano.data.repository.shared

import com.bruno13palhano.data.model.resource.Resource

/**
 * @param retries: Number of retries
 * @param call: The initial api call to be called
 * @param success: Callback to be called when the api call is successful
 */
internal suspend fun <T> remoteCallWithRetry(
    retries: Int = 3,
    call: suspend () -> Resource<T>,
    success: (response: T?) -> Unit,
    error: (message: String?) -> Unit,
) {
    var resource = call()
    var remainingTries = retries

    while (resource.data == null && remainingTries > 0) {
        remainingTries--
        resource = call()
    }

    when (resource) {
        is Resource.Success -> success(resource.data)

        is Resource.ResponseError -> error(resource.remoteResponseError?.description)

        is Resource.Error -> error(resource.errorType?.name)
    }
}
