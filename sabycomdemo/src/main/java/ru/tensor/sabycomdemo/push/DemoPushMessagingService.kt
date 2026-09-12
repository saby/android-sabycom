package ru.tensor.sabycomdemo.push

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.tensor.sabycom.Sabycom

/**
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
        Sabycom.sendToken(token)
    }
}