package ru.tensor.sabycomdemo.settings

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import java.util.*

/**
 * Виью модель настроек
 * Не используются LiveDate так как нет внешнего иточника данных
 *
 * @author ma.kolpakov
 */
internal class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val errorMessageLiveData = MutableLiveData<String>()
    val errorMessage: LiveData<String> = errorMessageLiveData

    private val showDemoLiveData = MutableLiveData<Unit>()
    val showDemo: LiveData<Unit> = showDemoLiveData

    var name: String = ""
    var surname: String = ""
    var phone: String = ""
    var email: String = ""
    var appId: String = ""
    var stand: Int = 0

    fun startWithData() {
        if (!validateData()) return

        Sabycom.initialize(getApplication(), appId)

        Sabycom.registerUser(UserData(UUID.randomUUID(), name, surname, email, phone))

        showDemoLiveData.value = Unit
    }

    fun startAnonymous() {
        if (!validateData()) return

        Sabycom.initialize(getApplication(), appId)

        Sabycom.registerAnonymous()

        showDemoLiveData.value = Unit
    }

    private fun validateData(): Boolean {
        appId.ifEmpty {
            errorMessageLiveData.value = "Поле appId бзязательно"
            return false
        }
        return true
    }
}