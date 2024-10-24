package ru.tensor.sabycom.push.builder

import android.content.Context
import androidx.core.app.NotificationCompat
import ru.tensor.sabycom.push.parser.data.PushNotificationMessage

/**
 * @author am.boldinov
 */
internal class MessagePushNotificationBuilder(
    private val context: Context
) : PushNotificationBuilder {

    override fun build(message: PushNotificationMessage): SabycomPushNotification {
        return SabycomPushNotification(context) {
            setTicker(message.text)
            setContentTitle(message.title)
            setContentText(message.text)
            setStyle(NotificationCompat.BigTextStyle().bigText(message.text))
           // setContentIntent() TODO 30.09.21 добавить после создания интента для экрана с виджетом
        }
    }

}