package ru.tensor.sabycomdemo.settings

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycomdemo.R
import ru.tensor.sabycomdemo.SabycomApp.Companion.APP_ID_KEY
import ru.tensor.sabycomdemo.SabycomApp.Companion.CURRENT_HOST_KEY
import ru.tensor.sabycomdemo.SabycomApp.Companion.DEFAULT_APP_ID
import ru.tensor.sabycomdemo.SabycomApp.Companion.SABYCOM_HOST_PREFS
import java.util.*

/**
 * Виью модель настроек
 *
 * @author ma.kolpakov
 */
internal class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        getApplication<Application>().getSharedPreferences(SABYCOM_HOST_PREFS, Context.MODE_PRIVATE)

    private val errorMessageLiveData = MutableLiveData<String>()
    val errorMessage: LiveData<String> = errorMessageLiveData

    private val showDemoLiveData = MutableLiveData<Event<Unit>>()
    val showDemo: LiveData<Event<Unit>> = showDemoLiveData

    //Не используются LiveDate так как нет внешнего иточника данных
    var name: String = ""
    var surname: String = ""
    var phone: String = ""
    var email: String = ""
    var appId: String = ""
    var host: Int = 0

    init {
        appId = sharedPreferences.getString(APP_ID_KEY, null) ?: DEFAULT_APP_ID
        host = getHostId(sharedPreferences.getString(CURRENT_HOST_KEY, null) ?: "prod")
    }

    fun startWithData() {
        if (!validateData()) return

        Sabycom.registerUser(UserData(UUID.randomUUID(), name, surname, email, phone))

        showDemoLiveData.value = Event(Unit)
    }

    fun startAnonymous() {
        if (!validateData()) return

        Sabycom.registerAnonymousUser()

        showDemoLiveData.value = Event(Unit)
    }

    private fun validateData(): Boolean {
        appId.ifEmpty {
            errorMessageLiveData.value = getApplication<Application>().getString(R.string.no_app_id_error)
            return false
        }
        return true
    }

    fun restart() {
        sharedPreferences.edit(true) {
            putString(CURRENT_HOST_KEY, getHostName(host))
            putString(APP_ID_KEY, appId)
        }
    }

    private fun getHostName(id: Int) = getApplication<Application>().resources.getStringArray(R.array.hosts)[id]
    private fun getHostId(name: String) =
        getApplication<Application>().resources.getStringArray(R.array.hosts).indexOf(name)
}
