package com.example.forecast.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.forecast.internal.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityInterceptorImpl(context: Context) : ConnectivityInterceptor {
    private val appContext=context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {

        if(!isOnline())
            throw NoConnectivityException()
        return chain.proceed((chain.request()))


    }

    private fun isOnline():Boolean{
        val connectivityManager=appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                    ||capabilities.hasTransport((NetworkCapabilities.TRANSPORT_CELLULAR))
                    ||capabilities.hasTransport((NetworkCapabilities.TRANSPORT_VPN)))
        } else {
            val network = connectivityManager.activeNetworkInfo
            return network!=null &&network.isConnected
        }


    }
}