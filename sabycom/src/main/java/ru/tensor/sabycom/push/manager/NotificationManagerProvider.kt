package ru.tensor.sabycom.push.manager

/**
 * @author am.boldinov
 */
internal interface NotificationManagerProvider {

    fun get(): NotificationManager
}