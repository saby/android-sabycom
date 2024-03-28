# android-sabycom
Быков Д. Ю. / Виджет чата поддержки для мобильных приложений

### Подключение
Для подключения SDK в фале build.gradle вашего модуля в секции dependencies необходимо добавить зависимость "ru.tensor.sabycom:1.0.0":

```groovy
dependencies {
//...
    implementation "ru.tensor.sabycom:1.0.0"
//...
}
```
### Структура проекта:

* **sabycom** - Исходный код SDK
* **sabycomdemo** - Пример реализации приложения с использованием SDK


### Использование SDK:

1. В вашем классе проиложения в методе onCreate сконфигурируйте Sabycom передав в качестве параметра идентификатор приложения.
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
4. Чтобы показать виджет, вызовите в вашей фрагменте `Sabycom.show(activity)`. Чтобы скрыть виджет, вызовите `Sabycom.hide()`.

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