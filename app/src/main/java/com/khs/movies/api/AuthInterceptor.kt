package com.khs.movies.api

import com.khs.movies.model.common.TOKEN
import com.khs.movies.util.ext.toApiKey
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        with(requestBuilder) {
            addHeader("Authorization", TOKEN.toApiKey())
        }

        return chain.proceed(requestBuilder.build())
    }
}