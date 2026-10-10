package ru.tensor.sabycom.push.lifecycle

import androidx.lifecycle.LiveData

/**
 * @author am.boldinov
 */
internal interface AppLifecycleTracker {

    fun isAppInForeground(): LiveData<Boolean>

    fun isAppInForegroundNow(): Boolean

    fun hasCreatedTasks(): Boolean
}