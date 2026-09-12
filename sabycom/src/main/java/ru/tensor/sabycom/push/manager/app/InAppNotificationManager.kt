package ru.tensor.sabycom.push.manager.app

import android.content.Context
import ru.tensor.sabycom.push.builder.SabycomNotification
import ru.tensor.sabycom.push.manager.NotificationManager
import ru.tensor.sabycom.push.util.registerForegroundTracker

/**
 * Менеджер для публикации/отмены публикации уведомлений внутри приложения (в виде всплывающего
 * сообщения поверх пользовательского интерфейса).
 * Публикует уведомления только если приложение находится на переднем плане.
 *
 * @author am.boldinov
 */
internal class InAppNotificationManager(
    context: Context,
) : NotificationManager {

    private val viewController = OverlayViewController()
    private val foregroundTracker = context.registerForegroundTracker {
        onBackground {
            publishedNotification?.apply {
                viewController.hide(it, tag, false)
            }
        }
        onForeground {
            publishedNotification?.apply {
                viewController.show(it, this, false)
            }
        }
    }

    private var publishedNotification: SabycomNotification? = null

    override fun notify(notification: SabycomNotification): Boolean {
        return notification.inAppBinder != null && foregroundTracker.launchOnForeground {
            publishedNotification = notification
            viewController.show(it, notification)
        }
    }

    override fun cancel(tag: String, id: Int) {
        foregroundTracker.launchOnForeground {
            viewController.hide(it, tag)
        }
        publishedNotification?.let {
            if (it.tag == tag && it.id == id) {
                publishedNotification = null
            }
        }
    }

    override fun cancelAll() {
        foregroundTracker.launchOnForeground {
            viewController.hideAll(it)
        }
        publishedNotification = null
    }
}