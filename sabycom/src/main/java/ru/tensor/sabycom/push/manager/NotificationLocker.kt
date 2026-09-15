package ru.tensor.sabycom.push.manager

/**
 * Интерфейс для управления блокировкой показа уведомлений.
 *
 * @author am.boldinov
 */
internal interface NotificationLocker {

    /**
     * Блокирует показ уведомлений.
     * Удаляет ранее опубликованные уведомления.
     */
    fun lock()

    /**
     * Разрешает показ уведомлений.
     */
    fun unlock()
}