package com.example.codechallenge.networking

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(var context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (isDeviceOnline(context)) {
            return chain.proceed(chain.request())
        } else {
            throw NoConnectivityException()
        }
    }
}

class NoConnectivityException : IOException() {
    override val message: String
        get() = "No Internet Connection"
}

@SuppressLint("ObsoleteSdkInt")
fun isDeviceOnline(applicationContext: Context): Boolean {
    val connManager = applicationContext.getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connManager.getNetworkCapabilities(connManager.activeNetwork)
        return if (networkCapabilities == null) {
            false
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_SUSPENDED)
            } else {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        }
    } else {
        val activeNetwork = connManager.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true && activeNetwork.isAvailable
    }
}