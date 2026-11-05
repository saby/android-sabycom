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
 * @author am.boldinov
 */
@SuppressLint("NewApi")
internal object NotificationChannelUtil {

    val DEFAULT_NOTIFICATION_CHANNEL_ID = buildChannelId("notification")

    fun submitDefaultNotificationChannel(context: Context, name: String): Boolean {
        return context.submitChannel {
            NotificationChannel(DEFAULT_NOTIFICATION_CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH)
        }
    }

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