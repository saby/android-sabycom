package ru.tensor.sabycom.push


/**
 * Ошибка при обработке неизвестного типа уведомления.
 * Может быть выброшена в случаях при парсинге невалидных данных или других ошибок
 * в процессе обработки входящего пуш-сообщения.
 *
 * @author am.boldinov
 */
class UnknownPushNotificationTypeException @JvmOverloads constructor(
    message: String? = null,
    throwable: Throwable? = null
) : IllegalArgumentException(message, throwable)