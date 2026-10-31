# android-sabycom
Быков Д. Ю. / Виджет чата поддержки для мобильных приложений

## Подключение
Для подключения SDK в фале build.gradle вашего модуля в секции dependencies необходимо добавить зависимость implementation 'ru.tensor.sabycom:sabycom:21.6219.63':

```groovy
dependencies {
//...
    implementation 'ru.tensor.sabycom:sabycom:21.6219.63'
//...
}
```
## Структура проекта

* **sabycom** - Исходный код SDK
* **sabycomdemo** - Пример реализации приложения с использованием SDK


## Использование SDK

### Установка виджета

1. В вашем классе приложения в методе onCreate сконфигурируйте Sabycom, передав в качестве параметра идентификатор приложения.
```kotlin
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Sabycom.initialize(applicationContext, "<My app id>")
    }
}
 ```
2. В зависимости от того, есть в вашем приложении авторизация или нет, зарегистрируйте пользователя или авторизуйте анонимно. 

 ```kotlin
class MyActivity : AppCompatActivity() {
    //...
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //...
        if (isLogined) {
            Sabycom.registerUser(
                UserData(
                    userId,
                    "Имя",
                    "Фамилия",
                    "email@google.com",
                    "79001234567"
                )
            )
        } else {
            Sabycom.registerAnonymousUser()
        }
        //...
    }
}
 ```
3. Вам так же необходимо позаботится о том что бы между сессиями userId для зарегестрированного пользователя был одинаковым. Например:
   
```kotlin
    fun getUserId(): UUID{
        val sharedPreferences = getSharedPreferences("PrefName", MODE_PRIVATE)
        var userIdString = sharedPreferences.getString("UserIdKey",null)
        if (userIdString == null){
            userIdString = UUID.randomUUID().toString()
            // edit is extensions from androidx
            sharedPreferences.edit { 
                putString("UserIdKey",userIdString)
            }
        }
        return requireNotNull(UUID.fromString(userIdString))
    }
```
4. Чтобы показать виджет, вызовите в вашем фрагменте `Sabycom.show(activity)`. Чтобы скрыть виджет, вызовите `Sabycom.hide()`.

 ```kotlin
class DemoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DemoFragmentBinding.inflate(inflater)

        binding.showSabycom.setOnClickListener {
            Sabycom.show(requireActivity() as AppCompatActivity)
        }
        
        return binding.root
    }
}
 ```

5. Для получения количества непрочитанных сообщений подпишитесь на обновления счетчика с помощью метода `Sabycom.unreadConversationCount(callback)` с коллбеком который будет вызван при каждом изменении счетчика.

```kotlin
class DemoViewModel : ViewModel() {
    private val messageCounterLiveData = MutableLiveData(0)
    val messageCounter: LiveData<Int> = messageCounterLiveData

    init {
        Sabycom.unreadConversationCount { count -> messageCounterLiveData.value = count }
    }

}
```

### Подписка на уведомления

1. Интегрируйте в ваш проект [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging/android/client).
2. Для подписки на уведомления получите токен от `FirebaseMessaging` и передайте его в Sabycom с помощью метода `Sabycom.sendToken(token)`.

```kotlin
FirebaseMessaging.getInstance()
    .token
    .addOnSuccessListener { token ->
        Sabycom.sendToken(token)
    }.addOnFailureListener { exception ->
        exception.printStackTrace()
    }
```

3. Для обеспечения показа уведомлений реализуйте сервис, расширяющий `FirebaseMessagingService`.
<br/>В методе `onMessageReceived` проверьте принадлежность данных push-сообщения виджету с помощью метода `Sabycom.isSabycomPushNotification(payload)`.
В случае если данные содержат информацию для отображения Sabycom уведомления метод вернет true и вам необходимо вызвать метод `Sabycom.handlePushNotification(payload)` для показа этого уведомления.
<br/>В методе `onNewToken` вам необходимо передать обновленный токен в Sabycom посредством вызова `Sabycom.sendToken(token)`.

```kotlin
internal class YourPushMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val payload = message.data
        // Проверяет, пришел push от Sabycom или от другого сервиса
        if (Sabycom.isSabycomPushNotification(payload)) {
            // Показывает всплывающее уведомление с новым сообщением
            Sabycom.handlePushNotification(payload)
        } else {
            //...
        }
    }

    override fun onNewToken(token: String) {
        Sabycom.sendToken(token)
    }
}
```

4. Зарегистрируйте сервис в `AndroidManifest.xml`.

```xml
<service
    android:name=".push.YourPushMessagingService"
    android:exported="false">
    <intent-filter>
        <action android:name="com.google.firebase.MESSAGING_EVENT" />
    </intent-filter>
</service>
```

5. Установите иконку для отображения в push-уведомлении. Иконка должна быть монохромной и соответствовать [стандартам](https://material.io/design/platform-guidance/android-notifications.html#anatomy-of-a-notification) .
<br/>Для создания иконки используйте [Image Asset Studio](https://developer.android.com/studio/write/image-asset-studio#access).

```xml
<meta-data
    android:name="ru.tensor.sabycom.push.notification_icon"
    android:resource="@drawable/your_notification_icon"/>
```

6. Вы так же можете установить какой цвет будет использоваться в push-уведомлении. По умолчанию берется атрибут `colorPrimary` из темы вашего приложения.
<br/>Версии Android 6.0 и ниже используют этот цвет в качестве фона для иконки уведомления. Начиная с версии Android 7.0 помимо цвета иконки дополнительно окрашивается название приложения.

```xml
<meta-data
    android:name="ru.tensor.sabycom.push.notification_color"
    android:resource="@color/your_notification_color"/>
```
