package ru.tensor.sabycomdemo.push

import android.content.Context
import com.google.firebase.messaging.FirebaseMessaging
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.push.ServiceType

/**
 * Реализация подписчика на получение новых push-уведомлений от Firebase Cloud Messaging.
 *
 * @author am.boldinov
 */
internal object DemoPushMessagingSubscriber : PushMessagingSubscriber {

    override fun subscribe(context: Context) {
        FirebaseMessaging.getInstance()
            .token
            .addOnSuccessListener { token ->
                Sabycom.sendToken(token, ServiceType.GOOGLE)
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}