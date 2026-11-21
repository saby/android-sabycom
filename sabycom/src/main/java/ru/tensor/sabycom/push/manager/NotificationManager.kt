package ru.tensor.sabycom.push.manager

import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * Интерфейс менеджера для публикации/отмены публикации уведомлений.
 *
 * @author am.boldinov
 */
internal interface NotificationManager {

    /**
     * Публикует уведомление для отображения пользователю.
     * @param notification объект уведомления
     *
     * @return true если публикация успешна, false иначе.
     */
    fun notify(notification: SabycomNotification): Boolean

    /**
     * Отменяет публикацию/удаляет ранее опубликованное уведомление.
     * @param tag тэг уведомления, под которым оно было опубликовано
     * @param id идентификатор уведомления
     * @see SabycomNotification.tag
     * @see SabycomNotification.id
     */
    fun cancel(tag: String, id: Int)

    /**
     * Отменяет публикации/удаляет все ранее опубликованные уведомления.
     */
    fun cancelAll()
}