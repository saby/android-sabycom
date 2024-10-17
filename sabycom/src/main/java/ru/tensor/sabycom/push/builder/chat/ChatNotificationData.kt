package ru.tensor.sabycom.push.builder.chat

import ru.tensor.sabycom.push.builder.NotificationData
import ru.tensor.sabycom.push.parser.data.PushCloudAction

/**
 * Данные уведомления по сообщению из чата виджета СБИС онлайн консультант.
 * @property dateTime дата отправки сообщения
 * @property avatarUrl ссылка на фото оператора
 * @property unreadCount кол-во непрочитанных сообщений
 *
 * @author am.boldinov
 */
internal data class ChatNotificationData(
    override val tag: String,
    override val id: Int,
    override val title: String,
    override val text: String,
    override val action: PushCloudAction,
    val dateTime: Long,
    val avatarUrl: String? = null,
    val unreadCount: Int
) : NotificationData