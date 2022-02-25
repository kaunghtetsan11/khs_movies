package com.khs.movies.model.common

import androidx.annotation.StringRes

sealed class CacheResult<out T> {

    data class Success<out T>(val value: T) : CacheResult<T>()

    data class GenericError(
        @StringRes val errorMessage: Int
    ) : CacheResult<Nothing>()
}