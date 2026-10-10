package ru.tensor.sabycom.push.parser.data

/**
 * Типы поддерживаемых пуш-уведомлений по событиям виджета СБИС онлайн консультант.
 *
 * @author am.boldinov
 */
internal enum class PushType(val value: Int) {
    CHAT(1),
    UNKNOWN(-1);

    companion object {

        fun fromValue(value: Int): PushType {
            return values().find {
                it.value == value
            } ?: UNKNOWN
        }
    }
}