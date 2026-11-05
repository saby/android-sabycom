package ru.tensor.sabycom.push.manager.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.core.app.NotificationManagerCompat
import ru.tensor.sabycom.push.builder.SabycomNotification
import ru.tensor.sabycom.push.manager.NotificationManager
import java.lang.Exception

/**
 * Менеджер для публикации/отмены публикации пуш-уведомлений
 * через системный [NotificationManagerCompat].
 * Уведомления отображаются в статус баре и на экране блокировки телефона.
 *
 * @author am.boldinov
 */
internal class PushNotificationManager(
    private val context: Context
) : NotificationManager {

    private val source = NotificationManagerCompat.from(context)

    override fun notify(notification: SabycomNotification): Boolean {
        val push = notification.pushBinder?.let {
            it.create(context).apply {
                it.bind(this, notification.data)
            }
        }?.build() ?: return false
        try {
            source.notify(notification.tag, notification.id, push)
        } catch (e: Exception) {
            Handler(Looper.getMainLooper()).post {
                try {
                    source.notify(notification.tag, notification.id, push)
                } catch (e: Exception) {
                }
            }
        }
        return true
    }

    override fun cancel(tag: String, id: Int) {
        source.cancel(tag, id)
    }

    override fun cancelAll() {
        source.cancelAll()
    }
}