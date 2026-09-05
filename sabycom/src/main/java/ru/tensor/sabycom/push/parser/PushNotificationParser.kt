package ru.tensor.sabycom.push.parser

import ru.tensor.sabycom.push.UnknownPushNotificationTypeException
import ru.tensor.sabycom.push.parser.data.PushNotificationMessage
import kotlin.jvm.Throws

/**
 * Парсер payload пуш-сообщения в нативную модель, которая является более лояльной к пользователю
 * и выставляется наружу для прикладных обработчиков уведомлений.
 *
 * @author am.boldinov
 */
internal interface PushNotificationParser {

    /**
     * Проверяет валидность данных пуш-уведомления.
     * @param payload данные по пуш-уведомлению от Messaging Service
     *
     * @return true если payload содержит корректные данные для отображения уведомлений, false иначе.
     */
    fun isValidPayload(payload: Map<String, String>): Boolean

    /**
     * Парсит "сырые" данные пуш-уведомления в data-модель для дальнейшей обработки.
     * В случае если данные являются невалидными бросает [UnknownPushNotificationTypeException].
     * @see isValidPayload
     * @param payload данные по пуш-уведомлению от Messaging Service
     *
     * @return модель пуш-сообщения, поступившего на устройство для обработки.
     */
    @Throws(UnknownPushNotificationTypeException::class)
    fun parse(payload: Map<String, String>): PushNotificationMessage
}