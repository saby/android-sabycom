package ru.tensor.sabycom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.push.PushNotificationCenter
import ru.tensor.sabycom.push.SabycomPushService
import ru.tensor.sabycom.widget.SabycomFeature
import ru.tensor.sabycom.widget.repository.SabycomRemoteRepository

/**
 * СБИС онлайн консультант.
 * @author ma.kolpakov
 */
object Sabycom : SabycomPushService {
    //region widget

    private var sabycomFeature: SabycomFeature? = null
    private var pushService: SabycomPushService? = null
    private val repository = SabycomRemoteRepository()

    /**
     * Инициализация компонента предпочтительно вызывать в onCreate вашего Application класса
     * @param context - контекст приложения
     * @param apiKey - API Ключ приложения
     */
    fun initialize(context: Context, apiKey: String) {
        check(sabycomFeature == null && pushService == null) { "Sabycom already initialized" }
        sabycomFeature = SabycomFeature(apiKey, repository)
        pushService = PushNotificationCenter(context, repository)
    }

    /**
     * Добавить информацию о пользователе. Метод должен быть вызван до [show]. Метод необходимо вызывать
     * даже если нет информации о пользователе, в таком случае необходимо передать только идентификатор
     * пользователя [UserData]
     */
    fun registerUser(userData: UserData) {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.registerUser(userData)
    }

    /**
     * Показать веб виджет
     * @throws IllegalStateException - если метод был вызван до [initialize] или пользователь не был зарегистрирован методом [registerUser]
     */
    fun show(activity: AppCompatActivity) {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.show(activity)
    }

    /**
     * Скрыть веб виджет
     */
    fun hide() {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.hide()
    }

    /**
     * Запросить количество непрочитанных сообщений асинхронно.
     * @param callback обратный вызов с результатом запроса
     */
    fun unreadConversationCount(callback: (Int) -> Unit) {
        // TODO: 14.09.2021 реализовать запрос количества непрочитанных сообщений https://online.sbis.ru/opendoc.html?guid=3966a770-62ae-4965-a6aa-732aea72b57c
    }

    //endregion

    //region push notification

    override fun isSabycomPushNotification(payload: Map<String, String>): Boolean {
        return checkNotNull(pushService) { NOT_INIT_ERROR }.isSabycomPushNotification(payload)
    }

    override fun handlePushNotification(payload: Map<String, String>) {
        checkNotNull(pushService) { NOT_INIT_ERROR }.handlePushNotification(payload)
    }

    override fun sendToken(token: String) {
        checkNotNull(pushService) { NOT_INIT_ERROR }.sendToken(token)
    }

    //endregion

    private const val NOT_INIT_ERROR =
        "Before using Sabycom, it is necessary to initialize in the Application class [Sabycom.initialize(<API key>)]"
}
