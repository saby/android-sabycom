package ru.tensor.sabycom.push.builder

import android.app.Notification
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import ru.tensor.sabycom.R
import ru.tensor.sabycom.push.util.NotificationChannelUtil

/**
 * Обертка над [NotificationCompat.Builder] для конфигурации по умолчанию.
 *
 * @author am.boldinov
 */
internal class NotificationCompatBuilder(
    val context: Context,
    category: String = DEFAULT_CATEGORY,
    channelId: String = NotificationChannelUtil.DEFAULT_NOTIFICATION_CHANNEL_ID
) : NotificationCompat.Builder(context, channelId) {

    init {
        setCategory(category)

        // design
        extractColor().apply {
            color = this
            setLights(this, LIGHT_ON_OFF_MS, LIGHT_ON_OFF_MS)
        }
        setSmallIcon(extractIcon())

        // rules
        setAutoCancel(true)
        priority = NotificationCompat.PRIORITY_HIGH

        //sound
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)?.let {
            setSound(it)
        }
    }

    @ColorInt
    private fun extractColor(): Int {
        val colorRes = context.extractResourceId(
            context.getString(R.string.sabycom_meta_push_notification_color),
            "com.google.firebase.messaging.default_notification_color",
            default = {
                context.getMetaInfo().let { info ->
                    info.theme.takeIf { it != 0 }?.let { theme ->
                        with(TypedValue()) {
                            ContextThemeWrapper(context, theme).theme.resolveAttribute(
                                R.attr.colorPrimary, this, true
                            )
                            resourceId
                        }
                    }.takeIf { it != 0 } ?: R.color.sabycom_notification_default_color
                }
            }
        )
        return ContextCompat.getColor(context, colorRes)
    }

    @DrawableRes
    private fun extractIcon(): Int {
        return context.extractResourceId(
            context.getString(R.string.sabycom_meta_push_notification_icon),
            "com.google.firebase.messaging.default_notification_icon",
            default = {
                context.getMetaInfo().let { info ->
                    info.icon.takeIf { it != 0 } ?: info.logo
                }
            }
        )
    }

    private inline fun Context.extractResourceId(vararg keys: String, default: () -> Int): Int {
        return extractMetaData({ meta ->
            keys.forEach { key ->
                meta.getInt(
                    key,
                    ResourcesCompat.ID_NULL
                ).takeIf { it != ResourcesCompat.ID_NULL }?.apply {
                    return@extractMetaData this
                }
            }
            null
        }, default)
    }

    private inline fun <reified T> Context.extractMetaData(
        extractor: (Bundle) -> T?,
        default: () -> T
    ): T {
        return getMetaInfo().metaData?.let(extractor) ?: default()
    }

    private fun Context.getMetaInfo(): ApplicationInfo {
        return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }

    companion object {

        private const val DEFAULT_CATEGORY = Notification.CATEGORY_MESSAGE
        private const val LIGHT_ON_OFF_MS = 1000
    }
}