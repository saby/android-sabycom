package ru.tensor.sabycom.push.builder.chat

import android.app.PendingIntent
import android.content.Context
import androidx.core.app.NotificationCompat
import ru.tensor.sabycom.BuildConfig
import ru.tensor.sabycom.push.builder.NotificationBuilder
import ru.tensor.sabycom.push.builder.SabycomNotification
import ru.tensor.sabycom.push.builder.binder.InAppNotificationBinder
import ru.tensor.sabycom.push.builder.NotificationCompatBuilder
import ru.tensor.sabycom.push.builder.binder.PushNotificationBinder
import ru.tensor.sabycom.push.manager.NotificationActionDispatcher
import ru.tensor.sabycom.push.parser.data.PushCloudAction
import ru.tensor.sabycom.push.parser.data.PushNotificationMessage
import ru.tensor.sabycom.widget.SabycomActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * Билдер по созданию уведомления по сообщениям из чата виджета СБИС онлайн консультант.
 *
 * @author am.boldinov
 */
internal class ChatNotificationBuilder(
    private val actionDispatcher: NotificationActionDispatcher
) : NotificationBuilder {

    override fun build(message: PushNotificationMessage): SabycomNotification {
        val avatarUrl = message.data.optString("operatorPhoto")
        val unreadCount = message.data.optInt("unreadCount")
        val tag = BuildConfig.LIBRARY_PACKAGE_NAME.plus(".").plus(message.type.name)
        val data = ChatNotificationData(
            tag,
            tag.hashCode(),
            message.title,
            message.text,
            message.action,
            message.sendTime,
            avatarUrl,
            unreadCount
        )
        return SabycomNotification.create(data, PushBinder(), InAppBinder(actionDispatcher))
    }

    private class PushBinder : PushNotificationBinder<ChatNotificationData> {

        override fun create(context: Context): NotificationCompatBuilder {
            return NotificationCompatBuilder(context)
        }

        override fun bind(view: NotificationCompatBuilder, data: ChatNotificationData) {
            view.apply {
                if (data.action == PushCloudAction.UPDATE) {
                    setOnlyAlertOnce(true)
                }
                setTicker(data.text)
                setContentTitle(data.title)
                setContentText(data.text)
                setStyle(NotificationCompat.BigTextStyle().bigText(data.text))
                setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        SabycomActivity.createIntent(context),
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }
        }
    }

    private class InAppBinder(
        private val actionDispatcher: NotificationActionDispatcher
    ) : InAppNotificationBinder<ChatNotificationData, ChatNotificationLayout> {

        override fun create(context: Context): ChatNotificationLayout {
            return ChatNotificationLayout(context)
        }

        override fun bind(view: ChatNotificationLayout, data: ChatNotificationData) {
            view.apply {
                setTitle(data.title)
                setSubtitle(data.text)
                setDate(formatDate(data.dateTime))
                setCounter(data.unreadCount)
                setAvatarUrl(data.avatarUrl)
                setOnCloseClickListener {
                    actionDispatcher.dispatchOnCancel(data.tag, data.id)
                }
                setOnClickListener {
                    actionDispatcher.dispatchOnCancel(data.tag, data.id)
                    it.context.startActivity(SabycomActivity.createIntent(it.context))
                }
            }
        }

        private fun formatDate(dateTime: Long): String {
            return SimpleDateFormat("d MMM", Locale.getDefault()).run {
                timeZone = TimeZone.getDefault()
                format(Date(dateTime))
            }
        }
    }
}