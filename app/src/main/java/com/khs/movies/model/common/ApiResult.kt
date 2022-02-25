package com.khs.movies.model.common

import androidx.annotation.StringRes

sealed class ApiResult<out T> {

    data class Success<out T>(val value: T) : ApiResult<T>()

    data class GenericError(
        var code: Int? = null,
        @StringRes val errorMessage: Int
    ) : ApiResult<Nothing>()

    object NetworkError : ApiResult<Nothing>()
}
