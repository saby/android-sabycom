package ru.tensor.sabycom.widget.network

import android.net.ConnectivityManager
import android.net.Network

/**
 * Отслеживает состояние сети, так же публикует событие об изменении статуса сети в listener.
 * Событие публикуется только в случае изменения сети, два одинаковых события не может быть.
 *
 * @author ma.kolpakov
 */
internal class NetworkWatcher(private val listener: (Boolean) -> Unit) : ConnectivityManager.NetworkCallback() {
    var isAvailable: Boolean = false
        private set

    override fun onAvailable(network: Network) {
        if (!isAvailable) {
            listener(true)
        }
        isAvailable = true
    }

    override fun onLost(network: Network) {
        if (isAvailable) {
            listener(false)
        }
        isAvailable = false
    }
}