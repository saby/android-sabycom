package ru.tensor.sabycom.push.parser

import ru.tensor.sabycom.push.parser.data.PushNotificationMessage

/**
 * @author am.boldinov
 */
internal interface PushNotificationParser {

    fun isValidPayload(payload: Map<String, String>): Boolean

    fun parse(payload: Map<String, String>): PushNotificationMessage
}