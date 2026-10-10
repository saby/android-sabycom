package ru.tensor.sabycom.push.builder.binder

import android.view.View
import ru.tensor.sabycom.push.builder.NotificationData

/**
 * Биндер для создания и привязки иерархии View с моделью уведомления для отображения
 * уведомлений внутри приложения (в виде всплывающего сообщения поверх пользовательского интерфейса).
 *
 * @author am.boldinov
 */
internal interface InAppNotificationBinder<DATA : NotificationData, V : View> :
    NotificationBinder<DATA, V>