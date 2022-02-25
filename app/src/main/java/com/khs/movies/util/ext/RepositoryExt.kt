package com.khs.movies.util.ext

import com.khs.movies.R
import com.khs.movies.model.common.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(
    @ApiMethod apiMethod: Int,
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
) = withContext(dispatcher) {
    try {
        // throws TimeoutCancellationException
        withTimeout(NETWORK_TIMEOUT) {
            ApiResult.Success(apiCall.invoke())
        }
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
        when (throwable) {
            is TimeoutCancellationException -> {
                val code = 408 // timeout error code
                ApiResult.GenericError(code, R.string.error_network_timeout)
            }
            is IOException -> {
                ApiResult.NetworkError
            }
            is HttpException -> {
                handleHttpException(
                    apiMethod = apiMethod,
                    errorCode = throwable.code()
                )
            }
            else -> {
                ApiResult.GenericError(
                    errorMessage = R.string.error_unknown
                )
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
) = withContext(dispatcher) {
    try {
        // throws TimeoutCancellationException
        withTimeout(CACHE_TIMEOUT) {
            CacheResult.Success(cacheCall.invoke())
        }
    } catch (throwable: Throwable) {
        when (throwable) {
            is TimeoutCancellationException -> {
                CacheResult.GenericError(R.string.error_cache_timeout)
            }
            else -> {
                throwable.printStackTrace()
                CacheResult.GenericError(R.string.error_db)
            }
        }
    }
}

private fun handleHttpException(
    @ApiMethod apiMethod: Int,
    errorCode: Int
): ApiResult<Nothing> {
    var tempErrorCode: Int? = null
    return ApiResult.GenericError(
        errorMessage = when (errorCode) {
            401 -> R.string.error_unknown
            else -> R.string.error_db
        }
    ).apply {
        tempErrorCode?.let {
            code = it
        }
    }
}