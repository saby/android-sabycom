package ru.tensor.sabycom.widget.network

import android.app.Application
import androidx.lifecycle.LiveData
/**
 * Интерфейс предоставляющий информацию о доступности сети, так же можно оформить подписку на изменение доступа
 *
 * @author ma.kolpakov
 */
internal interface NetworkAvailable {
    val internetAvailable: LiveData<Boolean>
    fun isNetworkAvailable(): Boolean
    fun bind(application: Application)
    fun unbind()

}