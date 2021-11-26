package ru.tensor.sabycom

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.push.PushNotificationCenter
import ru.tensor.sabycom.push.SabycomPushService
import ru.tensor.sabycom.widget.SabycomFeature
import ru.tensor.sabycom.widget.counter.UnreadCountController
import ru.tensor.sabycom.widget.counter.UnreadCounterCallback
import ru.tensor.sabycom.widget.repository.Repository
import ru.tensor.sabycom.widget.repository.SabycomLocalRepository
import ru.tensor.sabycom.widget.repository.SabycomRemoteRepository

/**
 * СБИС онлайн консультант.
 *
 * @author ma.kolpakov
 */
object Sabycom : SabycomPushService {
    //region widget

    private var pushService: SabycomPushService? = null
    internal var sabycomFeature: SabycomFeature? = null
    internal lateinit var repository: Repository
    internal lateinit var countController: UnreadCountController

    /**
     * Инициализация компонента предпочтительно вызывать в onCreate вашего Application класса
     * @param context - контекст приложения
     * @param apiKey - API Ключ приложения
     */
    fun initialize(context: Context, apiKey: String) {
        check(sabycomFeature == null && pushService == null) { "Sabycom already initialized" }
        repository = Repository(SabycomRemoteRepository(), SabycomLocalRepository(context))
        countController = UnreadCountController(repository)
        sabycomFeature = SabycomFeature(apiKey, repository)
        pushService = PushNotificationCenter(context, repository, countController)
    }

    /**
     * Добавить информацию о пользователе. Метод должен быть вызван до [show]. Метод необходимо вызывать
     * даже если нет информации о пользователе, в таком случае необходимо передать только идентификатор
     * пользователя [UserData]
     */
    fun registerUser(userData: UserData) {
        checkNotNull(sabycomFeature) { NOT_INIT_ERROR }.registerUser(userData)
        countController.requestCount()

    }
    /**
     * Зарегестрировать анонимного пользователя
     */
    fun registerAnonymous() {
        //todo implement
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
     * Зарегестрировать обработчик изсенения количества непрочитанных соощений.
     * Рузультаты будут доставленны в MainThread.
     * @param callback обработчик изменения количества непрочитанных сообщений.
     */
    fun unreadConversationCount(callback: UnreadCounterCallback) {
        countController.callback = callback
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

    internal const val NOT_INIT_ERROR =
        "Before using Sabycom, it is necessary to initialize in the Application class [Sabycom.initialize(<API key>)]"
}
