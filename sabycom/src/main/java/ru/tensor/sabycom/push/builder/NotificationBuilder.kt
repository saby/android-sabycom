package ru.tensor.sabycom.push.builder

import ru.tensor.sabycom.push.parser.data.PushNotificationMessage

/**
 * Билдер по созданию уведомления для отображения по конкретному типу данных на основе
 * поступившего на устройство пуш-сообщения.
 *
 * @author am.boldinov
 */
internal interface NotificationBuilder {

    /**
     * Создает модель уведомления для отображения.
     * @param message модель пуш-сообщения, поступившего на устройство для обработки
     */
    fun build(message: PushNotificationMessage): SabycomNotification
}