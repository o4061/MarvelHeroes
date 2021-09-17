package com.userfaltakas.marvelheroes.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

class NetworkManager : ConnectivityManager.NetworkCallback() {
    private var isNetworkAvailable: Boolean = false

    fun checkNetworkAvailability(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(this)
        var isConnected = false

        connectivityManager.allNetworks.forEach { network ->
            val networkCapability = connectivityManager.getNetworkCapabilities(network)
            networkCapability?.let {
                if (it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
                    isConnected = true
                    return@forEach
                }
            }
        }

        isNetworkAvailable = isConnected

        return isNetworkAvailable
    }

    override fun onAvailable(network: Network) {
        isNetworkAvailable = true
    }

    override fun onLost(network: Network) {
        isNetworkAvailable = false
    }
}