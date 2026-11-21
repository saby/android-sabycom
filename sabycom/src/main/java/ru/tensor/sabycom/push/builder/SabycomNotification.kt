package ru.tensor.sabycom.push.builder

import android.view.View
import ru.tensor.sabycom.push.builder.binder.InAppNotificationBinder
import ru.tensor.sabycom.push.builder.binder.PushNotificationBinder

/**
 * Модель уведомления для отображения.
 * @property data данные по уведомлению для биндинга
 * @property pushBinder биндер пуш-уведомлений
 * @property inAppBinder биндер всплывающих уведомлений внутри приложения
 * @property tag тэг уведомления, под которым оно будет опубликовано
 * @property id идентификатор уведомления
 *
 * @author am.boldinov
 */
internal class SabycomNotification private constructor(
    val data: NotificationData,
    val pushBinder: PushNotificationBinder<NotificationData>?,
    val inAppBinder: InAppNotificationBinder<NotificationData, View>?
) {

    val tag = data.tag
    val id = data.id

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <T : NotificationData> create(
            data: T,
            pushBinder: PushNotificationBinder<T>?,
            inAppBinder: InAppNotificationBinder<T, *>?
        ): SabycomNotification {
            return SabycomNotification(
                data,
                pushBinder as PushNotificationBinder<NotificationData>,
                inAppBinder as InAppNotificationBinder<NotificationData, View>
            )
        }
    }
}

