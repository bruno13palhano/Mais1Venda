package com.bruno13palhano.data.model.resource

sealed class Resource<T>(
    val data: T?,
    val errorType: ErrorType?,
    val remoteResponseError: RemoteResponseError?,
) {
    class Success<T>(data: T) : Resource<T>(data, null, null)
    class Error<T>(errorType: ErrorType?) : Resource<T>(null, errorType, null)
    class ResponseError<T>(
        remoteResponseError: RemoteResponseError?,
    ) : Resource<T>(null, null, remoteResponseError)
}
