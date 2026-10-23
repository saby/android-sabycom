package ru.tensor.sabycomdemo.settings

import android.app.Application
import android.content.Context
import android.view.View
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
import ru.tensor.sabycomdemo.SabycomApp.Companion.REGISTER_USER_ID_KEY
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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _showDemo = MutableLiveData<Event<Unit>>()
    val showDemo: LiveData<Event<Unit>> = _showDemo

    private val _restart = MutableLiveData<Event<Unit>>()
    val restart: LiveData<Event<Unit>> = _restart

    var name = MutableLiveData<String>()
    var surname = MutableLiveData<String>()
    var phone = MutableLiveData<String>()
    var email = MutableLiveData<String>()
    var appId = MutableLiveData(sharedPreferences.getString(APP_ID_KEY, null) ?: DEFAULT_APP_ID)
    var host = MutableLiveData(getHostId(sharedPreferences.getString(CURRENT_HOST_KEY, null) ?: getHostName(0)))

    @Suppress("UNUSED_PARAMETER")
    fun startWithData(view: View) {
        if (!validateData()) return

        Sabycom.registerUser(UserData(getUserId(), name.value, surname.value, email.value, phone.value))

        _showDemo.value = Event(Unit)
    }

    @Suppress("UNUSED_PARAMETER")
    fun resetRegisterUser(view: View) {
        sharedPreferences.edit {
            putString(REGISTER_USER_ID_KEY, null)
        }

        name.value = ""
        surname.value = ""
        email.value = ""
        phone.value = ""
    }

    @Suppress("UNUSED_PARAMETER")
    fun startAnonymous(view: View) {
        if (!validateData()) return

        Sabycom.registerAnonymousUser()

        _showDemo.value = Event(Unit)
    }

    @Suppress("UNUSED_PARAMETER")
    fun restart(view: View) {
        sharedPreferences.edit(true) {
            putString(CURRENT_HOST_KEY, getHostName(host.value!!))
            putString(APP_ID_KEY, appId.value)
        }
        _restart.value = Event(Unit)
    }

    private fun validateData(): Boolean {
        if (appId.value.isNullOrEmpty()) {
            _errorMessage.value = getApplication<Application>().getString(R.string.no_app_id_error)
            return false
        }
        return true
    }

    private fun getUserId(): UUID {
        var userIdString = sharedPreferences.getString(REGISTER_USER_ID_KEY, null)
        if (userIdString == null) {
            userIdString = UUID.randomUUID().toString()
            sharedPreferences.edit {
                putString(REGISTER_USER_ID_KEY, userIdString)
            }
        }
        return UUID.fromString(userIdString)
    }

    private fun getHostName(id: Int) = getApplication<Application>().resources.getStringArray(R.array.hosts)[id]
    private fun getHostId(name: String) =
        getApplication<Application>().resources.getStringArray(R.array.hosts).indexOf(name)
}
