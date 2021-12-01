package ru.tensor.sabycom.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import ru.tensor.sabycom.push.builder.chat.ChatNotificationBuilder
import ru.tensor.sabycom.push.builder.NotificationBuilder
import ru.tensor.sabycom.push.parser.PushNotificationParser
import ru.tensor.sabycom.push.parser.SabycomPushNotificationParser
import ru.tensor.sabycom.push.parser.data.PushType
import ru.tensor.sabycom.push.manager.CompositeNotificationManager
import ru.tensor.sabycom.push.manager.NotificationActionDispatcher
import ru.tensor.sabycom.push.manager.NotificationLocker
import ru.tensor.sabycom.push.manager.app.InAppNotificationManager
import ru.tensor.sabycom.push.manager.push.PushNotificationManager
import ru.tensor.sabycom.push.parser.data.PushCloudAction
import ru.tensor.sabycom.push.util.NotificationChannelUtil
import ru.tensor.sabycom.widget.counter.IUnreadCountController
import ru.tensor.sabycom.widget.repository.Repository
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Представляет собой точку входа для обработки событий по пуш-уведомлениям.
 * Получает новые события от Messaging Service, обрабатывает и передает управление
 * готовыми данными по пушам прикладным обработчикам, которые хранит на всё время жизни компонента.
 * Регистрирует основные каналы уведомлений для публикации.
 *
 * @author am.boldinov
 */
internal class PushNotificationCenter(
    private val context: Context,
    private val repository: Repository,
    private val countController: IUnreadCountController,
    private val parser: PushNotificationParser
) : SabycomPushService, NotificationActionDispatcher, NotificationLocker {

    constructor(
        context: Context,
        repository: Repository,
        countController: IUnreadCountController
    ) : this(
        context,
        repository,
        countController,
        SabycomPushNotificationParser()
    )

    private val builderMap = mutableMapOf<PushType, NotificationBuilder>()
    private val notificationManager = CompositeNotificationManager(
        InAppNotificationManager(context),
        PushNotificationManager(context)
    )
    private val handler = Handler(Looper.getMainLooper())
    private val locked = AtomicBoolean()

    init {
        initNotificationChannels()
        initPushBuilders()
    }

    override fun isSabycomPushNotification(payload: Map<String, String>): Boolean {
        return parser.isValidPayload(payload)
    }

    override fun handlePushNotification(payload: Map<String, String>) {
        val message = parser.parse(payload)
        if (message.addresseeId == repository.getUserData().id.toString()) {
            countController.requestCount()
            builderMap[message.type]?.build(message)?.apply {
                handler.post {
                    if (!locked.get()) {
                        if (data.action == PushCloudAction.CANCEL) {
                            notificationManager.cancel(tag, id)
                        } else {
                            notificationManager.notify(this)
                        }
                    }
                }
            }
        } else {
            Log.d(
                "PushNotificationCenter",
                "Push notification received for different users or the current user is null, addresseeId: ${message.addresseeId}"
            )
        }
    }

    override fun sendToken(token: String) {
        repository.sendPushToken(token)
    }

    override fun dispatchOnCancel(tag: String, id: Int) {
        notificationManager.cancel(tag, id)
    }

    override fun dispatchOnCancelAll() {
        notificationManager.cancelAll()
    }

    override fun lock() {
        dispatchOnCancelAll()
        locked.set(true)
    }

    override fun unlock() {
        locked.set(false)
    }

    private fun initNotificationChannels() {
        NotificationChannelUtil.submitDefaultNotificationChannel(context, "Sabycom") // TODO name https://online.sbis.ru/opendoc.html?guid=31a7f99e-60c1-4168-b904-bb150f0e75f5
    }

    private fun initPushBuilders() {
        builderMap[PushType.CHAT] = ChatNotificationBuilder(this)
    }
}