package ru.tensor.sabycomdemo.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.push.ServiceType

/**
 * Реализация сервиса для получения новых push-сообщений от Firebase Cloud Messaging и
 * событий обновления токена для обеспечения подписки.
 *
 * @author am.boldinov
 */
internal class DemoPushMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = message.data
        if (Sabycom.isSabycomPushNotification(payload)) {
            Sabycom.handlePushNotification(payload)
        } else {
            // custom processing
        }
    }

    override fun onNewToken(token: String) {
        Sabycom.sendToken(token, ServiceType.GOOGLE)
    }
}