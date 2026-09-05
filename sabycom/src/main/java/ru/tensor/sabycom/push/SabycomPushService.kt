package ru.tensor.sabycom.push

import androidx.annotation.AnyThread
import kotlin.jvm.Throws

/**
 * Сервис для диспетчеризации пуш-уведомлений, представляет собой точку
 * входа для обработки событий.
 *
 * @author am.boldinov
 */
@AnyThread
interface SabycomPushService {

    /**
     * Проверяет принадлежность данных пуш-уведомления виджету СБИС онлайн консультант.
     * @param payload данные по пуш-уведомлению, полученные от Messaging Service
     *
     * @return true если пуш-уведомление является событием виджета, false иначе.
     * @see handlePushNotification
     */
    fun isSabycomPushNotification(payload: Map<String, String>): Boolean

    /**
     * Обрабатывает модель пуш-сообщения, поступившего на устройство, и выполняет отображение уведомления.
     * В случае если данные являются невалидными или не содержат информации для отображения
     * уведомления по событию виджета СБИС онлайн консультант бросает [UnknownPushNotificationTypeException].
     * @see isSabycomPushNotification
     * @param payload данные по пуш-уведомлению, полученные от Messaging Service
     */
    @Throws(UnknownPushNotificationTypeException::class)
    fun handlePushNotification(payload: Map<String, String>)

    /**
     * Обрабатывает токен, полученный от Messaging Service, для обеспечения подписки на
     * пуш-уведомления по событиям виджета СБИС онлайн консультант.
     * @param token токен регистрации на сервисе Cloud Messaging для текущего проекта
     */
    fun sendToken(token: String)
}