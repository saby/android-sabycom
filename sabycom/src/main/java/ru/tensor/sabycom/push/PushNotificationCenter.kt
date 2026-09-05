package ru.tensor.sabycom.push

import android.content.Context
import android.os.Handler
import android.os.Looper
import ru.tensor.sabycom.push.builder.MessagePushNotificationBuilder
import ru.tensor.sabycom.push.builder.PushNotificationBuilder
import ru.tensor.sabycom.push.lifecycle.AppLifecycleTracker
import ru.tensor.sabycom.push.lifecycle.SabycomLifecycleTracker
import ru.tensor.sabycom.push.manager.NotificationManager
import ru.tensor.sabycom.push.manager.NotificationManagerProvider
import ru.tensor.sabycom.push.manager.PushNotificationManager
import ru.tensor.sabycom.push.manager.ToastNotificationManager
import ru.tensor.sabycom.push.parser.PushNotificationParser
import ru.tensor.sabycom.push.parser.SabycomPushNotificationParser
import ru.tensor.sabycom.push.parser.data.PushType
import ru.tensor.sabycom.push.util.NotificationChannelUtil
import ru.tensor.sabycom.widget.counter.IUnreadCountController
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author am.boldinov
 */
internal class PushNotificationCenter(
    private val context: Context,
    private val repository: Repository,
    private val countController: IUnreadCountController,
    private val lifecycleTracker: AppLifecycleTracker,
    private val parser: PushNotificationParser
) : SabycomPushService {

    constructor(
        context: Context,
        repository: Repository,
        countController: IUnreadCountController
    ) : this(
        context,
        repository,
        countController,
        SabycomLifecycleTracker(context),
        SabycomPushNotificationParser()
    )

    private val managerProvider = NotificationManagerHolder()
    private val pushBuilderMap = mutableMapOf<PushType, PushNotificationBuilder>()
    private val handler = Handler(Looper.getMainLooper())

    init {
        initNotificationChannels()
        initPushBuilders()
    }

    override fun isSabycomPushNotification(payload: Map<String, String>): Boolean {
        return parser.isValidPayload(payload)
    }

    override fun handlePushNotification(payload: Map<String, String>) {
        val message = parser.parse(payload)
        countController.requestCount()
        pushBuilderMap[message.type]?.build(message)?.let { notification ->
            handler.post {
                managerProvider.get().notify("TAG", 0, notification.build()) // TODO tag & id
            }
        }
    }

    override fun sendToken(token: String) {
        repository.sendPushToken(token)
    }

    private fun initNotificationChannels() {
        NotificationChannelUtil.submitDefaultNotificationChannel(context, "САБИДОК") // TODO name
    }

    private fun initPushBuilders() {
        pushBuilderMap[PushType.MESSAGE] = MessagePushNotificationBuilder(context)
    }

    private inner class NotificationManagerHolder : NotificationManagerProvider {

        private val pushManager by lazy { PushNotificationManager(context) }
        private val toastManager by lazy { ToastNotificationManager(context) }

        override fun get(): NotificationManager {
            return if (lifecycleTracker.isAppInForegroundNow()) {
                toastManager
            } else {
                pushManager
            }
        }

    }
}