package com.bruno13palhano.data.datasource.remote.source

import com.bruno13palhano.data.model.resource.ErrorType
import com.bruno13palhano.data.model.resource.RemoteResponseError
import com.bruno13palhano.data.model.resource.Resource
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.io.IOException
import retrofit2.HttpException
import retrofit2.Response

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

internal suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
    return try {
        getResponse(apiCall.invoke())
    } catch (e: HttpException) {
        Resource.Error(errorType = ErrorType.SERVER)
    } catch (e: IOException) {
        Resource.Error(errorType = ErrorType.NO_INTERNET)
    } catch (e: Exception) {
        Resource.Error(errorType = ErrorType.UNKNOWN)
    }
}

private fun <T> getResponse(response: Response<T>): Resource<T> {
    val result = response.body()

    return if (response.isSuccessful && result != null) {
        Resource.Success(result)
    } else {
        Resource.ResponseError(
            remoteResponseError = getInvalidResponse(response.errorBody()!!.string()),
        )
    }
}

private fun getInvalidResponse(response: String): RemoteResponseError {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    return moshi.adapter(RemoteResponseError::class.java).fromJson(response)!!
}
