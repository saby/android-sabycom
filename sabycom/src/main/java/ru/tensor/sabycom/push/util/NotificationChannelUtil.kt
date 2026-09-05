package ru.tensor.sabycom.push.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import ru.tensor.sabycom.BuildConfig
import java.lang.IllegalArgumentException

/**
 * Утилита для работы с каналами уведомлений.
 *
 * @author am.boldinov
 */
@SuppressLint("NewApi")
internal object NotificationChannelUtil {

    val DEFAULT_NOTIFICATION_CHANNEL_ID = buildChannelId("notification")

    /**
     * Создает канал по умолчанию для публикации уведомлений в [NotificationManager]
     * с указанным именем.
     * @param context контекст приложения
     * @param name название канала по умолчанию
     *
     * @return true если каналы поддерживаются устройством и канал был успешно создан, false иначе
     */
    fun submitDefaultNotificationChannel(context: Context, name: String): Boolean {
        return context.submitChannel {
            NotificationChannel(
                DEFAULT_NOTIFICATION_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_HIGH
            ).apply { setShowBadge(false) }
        }
    }

    /**
     * Проверяет поддержку каналов уведомлений.
     * Каналы доступны начиная с Android Oreo.
     *
     * @return true если каналы поддерживаются, false иначе
     */
    @Suppress("MemberVisibilityCanBePrivate")
    fun isSupportChannels(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private inline fun Context.submitChannel(creator: (NotificationManagerCompat) -> NotificationChannel): Boolean {
        if (isSupportChannels()) {
            val manager = NotificationManagerCompat.from(this)
            val channel = creator.invoke(manager)
            if (assertChannelId(channel.id)) {
                channel.enableLights(true)
                channel.enableVibration(true)
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)?.let {
                    channel.setSound(it, Notification.AUDIO_ATTRIBUTES_DEFAULT)
                }
                manager.createNotificationChannel(channel)
                return true
            }
        }
        return false
    }

    @Suppress("SameParameterValue")
    private fun buildChannelId(uniqueToken: String): String {
        return BuildConfig.LIBRARY_PACKAGE_NAME.plus(".").plus(uniqueToken)
    }

    private fun assertChannelId(id: String): Boolean {
        if (id.isEmpty()) {
            throw IllegalArgumentException("Notification channel id is empty")
        }
        if (id == NotificationChannel.DEFAULT_CHANNEL_ID) {
            throw IllegalArgumentException("Notification channel id $id is reserved by android")
        }
        return true
    }
}