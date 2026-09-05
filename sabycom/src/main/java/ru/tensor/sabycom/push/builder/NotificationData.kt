package ru.tensor.sabycom.push.builder

import ru.tensor.sabycom.push.parser.data.PushCloudAction

/**
 * Базовый интерфейс для модели данных уведомления, описывающий обязательные свойства.
 * @property tag тэг уведомления, под которым оно будет опубликовано
 * @property id идентификатор уведомления
 * @property title заголовок уведомления
 * @property text текст уведомления
 * @property action действие с уведомлением
 *
 * @author am.boldinov
 */
internal interface NotificationData {
    val tag: String
    val id: Int
    val title: String
    val text: String
    val action: PushCloudAction
}