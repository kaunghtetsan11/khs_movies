package com.khs.movies.util.framework

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import com.khs.movies.model.common.DISCONNECTED
import com.khs.movies.model.common.MOBILE_DATA_CONNECTED
import com.khs.movies.model.common.WIFI_CONNECTED
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveConnection @Inject constructor(@ApplicationContext context: Context) :
    LiveData<Int>() {

    private val connectivityManager by lazy {
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val connectivityManagerCallback by lazy {
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateConnection(network)
            }

            override fun onLost(network: Network) {
                updateConnection(network)
            }
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onActive() {
        super.onActive()
        updateConnection(connectivityManager.activeNetwork)
        connectivityManager.registerDefaultNetworkCallback(connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun updateConnection(network: Network?) {
        network?.let {
            scope.launch {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(it)
                if (networkCapabilities != null && isOnline())
                    postValue(
                        when {
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->
                                MOBILE_DATA_CONNECTED
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->
                                WIFI_CONNECTED
                            else -> DISCONNECTED
                        }
                    ) else postValue(DISCONNECTED)
            }
        } ?: postValue(DISCONNECTED)
    }

    private fun isOnline(): Boolean {
        return try {
            val sock = Socket()
            sock.connect(InetSocketAddress("8.8.8.8", 53), 1500)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }
    }
}