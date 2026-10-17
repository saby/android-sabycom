package ru.tensor.sabycom.push.manager

import android.app.Notification
import android.content.Context
import android.widget.Toast

/**
 * @author am.boldinov
 */
internal class ToastNotificationManager(
    private val context: Context,
) : NotificationManager {

    override fun notify(tag: String, id: Int, notification: Notification) {
        // TODO: 29.09.2021 доработать когда отрисуют уведомление
        Toast.makeText(context, notification.tickerText, Toast.LENGTH_SHORT).show()
    }

    override fun cancel(tag: String, id: Int) {
        // ignore
    }
}