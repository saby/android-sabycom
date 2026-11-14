package ru.tensor.sabycom.push.parser.data

/**
 * Типы действий с пуш уведомлением, которые может инициировать облако при поступлении пуша на устройство.
 *
 * @author am.boldinov
 */
internal enum class PushCloudAction(val value: Int) {
    CANCEL(0), // отменяет полученное ранее уведомление (удалить пуш)
    NOTIFY(1), // уведомляет пользователя о новом уведомлении (показать пуш)
    UPDATE(2); // обновляет полученнное ранее уведомление (обновить информацию в пуше)

    companion object {

        fun fromValue(value: Int): PushCloudAction {
            return values().find {
                it.value == value
            } ?: NOTIFY
        }
    }
}
