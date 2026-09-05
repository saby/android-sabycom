package ru.tensor.sabycomdemo.push

import android.content.Context

/**
 * Интерфейс-контракт подписчика на push-уведомления.
 *
 * @author am.boldinov
 */
interface PushMessagingSubscriber {

    /**
     * Производит подписку на push-уведомления.
     * Получает токен регистрации от мобильных сервисов и отправляет его в [ru.tensor.sabycom.Sabycom].
     *
     * @param context контекст приложения
     */
    fun subscribe(context: Context)
}