package ru.tensor.sabycom.push


/**
 * @author am.boldinov
 */
class UnknownPushNotificationTypeException @JvmOverloads constructor(
    message: String? = null,
    throwable: Throwable? = null
) : IllegalArgumentException(message, throwable)