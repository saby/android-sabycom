package ru.tensor.sabycomdemo.push

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.push.ServiceType

/**
 * Реализация сервиса для получения новых push-сообщений от Huawei Cloud Messaging и
 * событий обновления токена для обеспечения подписки.
 *
 * @author am.boldinov
 */
internal class DemoPushMessagingService : HmsMessageService() {

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        val payload = message?.dataOfMap
        if (payload != null && Sabycom.isSabycomPushNotification(payload)) {
            Sabycom.handlePushNotification(payload)
        } else {
            // custom processing
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        if (!token.isNullOrEmpty()) {
            Sabycom.sendToken(token, ServiceType.HUAWEI)
        }
    }
}