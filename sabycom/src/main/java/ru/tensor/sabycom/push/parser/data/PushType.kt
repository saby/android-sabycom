package ru.tensor.sabycom.push.parser.data

/**
 * @author am.boldinov
 */
internal enum class PushType(val value: Int) {
    MESSAGE(1),
    UNKNOWN(-1);

    companion object {

        fun fromValue(value: Int): PushType {
            return values().find {
                it.value == value
            } ?: UNKNOWN
        }
    }
}