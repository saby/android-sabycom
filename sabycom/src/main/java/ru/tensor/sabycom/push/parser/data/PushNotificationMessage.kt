package ru.tensor.sabycom.push.parser.data

import org.json.JSONObject

/**
 * Модель пуш-сообщения, поступившего на устройство для обработки.
 *
 * @property id уникальный идентификатор уведомления
 * @property title заголовок пуш-уведомления по умолчанию, можно переопределить при публикации
 * @property text текст пуш-уведомления по умолчанию, можно переопределить при публикации
 * @property type тип пуш-уведомления
 * @property sendTime дата отправки уведомления в миллисекундах
 * @property addresseeId идентификатор пользователя, которому адресуется данный пуш
 * @property action предполагаемое действие с пуш-уведомлением
 * @property data json с прикладными данными по пуш-уведомлению
 *
 * @author am.boldinov
 */
internal class PushNotificationMessage(
    val id: String,
    val title: String,
    val text: String,
    val type: PushType,
    val sendTime: Long,
    val addresseeId: String,
    val action: PushCloudAction,
    val data: JSONObject
)