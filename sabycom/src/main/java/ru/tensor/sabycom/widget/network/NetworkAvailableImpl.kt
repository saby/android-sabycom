package ru.tensor.sabycom.widget.network

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData

internal class NetworkAvailableImpl : NetworkAvailable {

    override val internetAvailable: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    override fun isNetworkAvailable() = networkWatcher.isAvailable

    private lateinit var connectivityManager: ConnectivityManager

    private lateinit var networkWatcher: NetworkWatcher

    override fun bind(application: Application) {
        connectivityManager = application.getSystemService(ConnectivityManager::class.java)

        networkWatcher = NetworkWatcher() {
            internetAvailable.postValue(it)
        }
        connectivityManager.registerDefaultNetworkCallback(networkWatcher)
    }

    override fun unbind() {
        connectivityManager.unregisterNetworkCallback(networkWatcher)
    }
}