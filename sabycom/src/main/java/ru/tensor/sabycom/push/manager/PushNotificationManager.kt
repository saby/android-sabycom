package ru.tensor.sabycom.push.manager

import android.app.Notification
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception

/**
 * @author am.boldinov
 */
internal class PushNotificationManager(
    context: Context
) : NotificationManager {

    private val source = NotificationManagerCompat.from(context)

    override fun notify(tag: String, id: Int, notification: Notification) {
        try {
            source.notify(tag, id, notification)
        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                try {
                    source.notify(tag, id, notification)
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun cancel(tag: String, id: Int) {
        source.cancel(tag, id)
    }
}