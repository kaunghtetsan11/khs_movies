package com.khs.movies.util.framework

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.*

object AppConnectivityManager {
    fun isOnline(): Boolean = runBlocking {
        return@runBlocking withContext(Dispatchers.IO) { checkOnline() }
    }

    private fun checkOnline(): Boolean {
        return try {
            InetAddress.getByName("www.google.com")
            true
        } catch (e: UnknownHostException) {
            false
        }
    }
}