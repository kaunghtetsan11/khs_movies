package com.khs.movies.util.ext

fun String.toApiKey(): String = "Bearer $this"