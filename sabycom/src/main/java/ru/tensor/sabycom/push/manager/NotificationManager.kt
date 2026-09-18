package ru.tensor.sabycom.push.manager

import android.app.Notification

/**
 * @author am.boldinov
 */
internal interface NotificationManager {

    fun notify(tag: String, id: Int, notification: Notification)

    fun cancel(tag: String, id: Int)
}