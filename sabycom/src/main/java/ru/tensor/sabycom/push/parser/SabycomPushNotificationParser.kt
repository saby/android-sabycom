package ru.tensor.sabycom.push.parser

import org.json.JSONObject
import ru.tensor.sabycom.push.UnknownPushNotificationTypeException
import ru.tensor.sabycom.push.parser.data.PushCloudAction
import ru.tensor.sabycom.push.parser.data.PushNotificationMessage
import ru.tensor.sabycom.push.parser.data.PushType
import java.lang.Exception

/**
 * Реализация парсера пуш-уведомлений по событиям виджета СБИС онлайн консультант.
 *
 * @author am.boldinov
 */
internal class SabycomPushNotificationParser : PushNotificationParser {

    override fun isValidPayload(payload: Map<String, String>): Boolean {
        return payload.containsKeys(*KeyContract.values())
    }

    override fun parse(payload: Map<String, String>): PushNotificationMessage {
        try {
            val system = JSONObject(payload.get(KeyContract.SYSTEM))
            val type = payload.get(KeyContract.TYPE).toInt()
            val action = payload.get(KeyContract.ACTION).toInt()
            return PushNotificationMessage(
                payload.get(KeyContract.MESSAGE_ID),
                system.getString("title"),
                system.getString("body"),
                PushType.fromValue(type),
                payload.get(KeyContract.TIMESTAMP).toLong(),
                payload.get(KeyContract.ADDRESSEE_ID),
                PushCloudAction.fromValue(action),
                JSONObject(payload.get(KeyContract.DATA))
            )
        } catch (e: Exception) {
            throw UnknownPushNotificationTypeException(throwable = e)
        }
    }

    private fun Map<String, String>.containsKeys(vararg keys: KeyContract): Boolean {
        keys.forEach {
            if (!containsKey(it.key)) {
                return false
            }
        }
        return true
    }

    private fun Map<String, String>.get(key: KeyContract): String {
        return getValue(key.key)
    }

    private enum class KeyContract(val key: String) {
        MESSAGE_ID("id"),
        ACTION("action"),
        ADDRESSEE_ID("addresseeId"),
        SYSTEM("system"),
        TYPE("type"),
        TIMESTAMP("timestamp"),
        DATA("data")
    }
}