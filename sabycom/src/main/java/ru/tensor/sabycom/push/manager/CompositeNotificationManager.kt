package ru.tensor.sabycom.push.manager

import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * Реализация менеджера для последовательной передачи управления всем существующим менеджерам.
 * Необходимо использовать в случае наличия нескольких менеджеров для работы с уведомлениями,
 * каждый из которых публикует уведомления в разные части интерфейса.
 *
 * @author am.boldinov
 */
internal class CompositeNotificationManager(
    vararg managers: NotificationManager
) : NotificationManager {

    private val sources = managers

    /**
     * Передает управление на публикацию только одному менеджеру, который успешно
     * опубликует уведомление.
     * Удалит опубликованные уведомления в случае их наличия в других менеджерах с аналогичными
     * [SabycomNotification.tag] и [SabycomNotification.id]
     */
    override fun notify(notification: SabycomNotification): Boolean {
        return sources.find {
            it.notify(notification)
        }?.let { notify ->
            sources.forEach {
                if (it != notify) {
                    it.cancel(notification.tag, notification.id)
                }
            }
            true
        } ?: false
    }

    override fun cancel(tag: String, id: Int) {
        sources.forEach {
            it.cancel(tag, id)
        }
    }

    override fun cancelAll() {
        sources.forEach {
            it.cancelAll()
        }
    }
}