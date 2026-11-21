package ru.tensor.sabycom.push.builder

import android.app.Notification
import android.content.Context
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import ru.tensor.sabycom.R
import ru.tensor.sabycom.push.util.NotificationChannelUtil

/**
 * @author am.boldinov
 */
internal class SabycomPushNotification(
    private val context: Context,
    category: String = DEFAULT_CATEGORY,
    channelId: String = NotificationChannelUtil.DEFAULT_NOTIFICATION_CHANNEL_ID,
    builderBlock: NotificationCompat.Builder.() -> Unit
) {

    private val builder = NotificationCompat.Builder(context, channelId).apply {
        setCategory(category)
        configureDefault()
        builderBlock.invoke(this)
    }

    private fun NotificationCompat.Builder.configureDefault() {
        // design
        // setColor(ContextCompat.getColor(context)) TODO 30.09.2021 установить иконки виджета, либо использовать иконки приложения
        setSmallIcon(android.R.drawable.ic_dialog_info)

        // rules
        setAutoCancel(true)
        priority = NotificationCompat.PRIORITY_HIGH

        // lights
//        setLights(
//            ContextCompat.getColor(context, R.color.design_default_color_primary), // TODO 30.09.2021 цвет индикатора
//            LIGHT_ON_OFF_MS,
//            LIGHT_ON_OFF_MS
//        )

        //sound
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)?.let {
            setSound(it)
        }

        // vibration
        (context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?)?.let {
            if (it.hasVibrator()) {
                setVibrate(
                    longArrayOf(
                        VIBRATION_PATTERN_ELEMENT,
                        VIBRATION_PATTERN_ELEMENT,
                        VIBRATION_PATTERN_ELEMENT
                    )
                )
            }
        }
    }

    fun build(): Notification = builder.build()

    companion object {

        private const val DEFAULT_CATEGORY = Notification.CATEGORY_MESSAGE
        private const val LIGHT_ON_OFF_MS = 1000
        private const val VIBRATION_PATTERN_ELEMENT = 500L
    }

}