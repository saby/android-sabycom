package ru.tensor.sabycom.push.builder

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.tensor.sabycom.push.parser.data.PushNotificationMessage
import ru.tensor.sabycom.widget.SabycomActivity

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
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    0,
                    SabycomActivity.createIntent(context),
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            )
        }
    }

}