package ru.tensor.sabycom.push.builder

import ru.tensor.sabycom.push.parser.data.PushNotificationMessage

/**
 * @author am.boldinov
 */
internal interface PushNotificationBuilder {

    fun build(message: PushNotificationMessage): SabycomPushNotification
}