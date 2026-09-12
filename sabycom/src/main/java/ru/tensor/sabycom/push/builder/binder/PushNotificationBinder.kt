package ru.tensor.sabycom.push.builder.binder

import ru.tensor.sabycom.push.builder.NotificationCompatBuilder
import ru.tensor.sabycom.push.builder.NotificationData

/**
 * Биндер для создания и привязки системного билдера пуш-уведомлений с моделью уведомления
 * для отображения пуш-уведомлений в системном баре.
 *
 * @author am.boldinov
 */
internal interface PushNotificationBinder<DATA : NotificationData> :
    NotificationBinder<DATA, NotificationCompatBuilder>