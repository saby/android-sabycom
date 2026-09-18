package ru.tensor.sabycomdemo

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import java.util.UUID

/**
 * @author ma.kolpakov
 */
class DemoViewModel(application: Application) : AndroidViewModel(application) {
    private val _messageCounter = MutableLiveData(0)
    private val _showEvent = MutableLiveData<Unit>()
    val messageCounter: LiveData<Int> = _messageCounter
    val showEvent: LiveData<Unit> = _showEvent

    init {

        val sharedPreferences = application.getSharedPreferences(SHARED_PREFERENCE_NAME, AppCompatActivity.MODE_PRIVATE)

        var stringUUID = sharedPreferences.getString(UUID_KEY, null)
        if (stringUUID.isNullOrEmpty()) {
            stringUUID = UUID.randomUUID().toString()
            val editor = sharedPreferences.edit()
            editor.putString(UUID_KEY, stringUUID)
            editor.apply()
        }

        Sabycom.registerUser(UserData(UUID.fromString(stringUUID)))

        Sabycom.unreadConversationCount {
            _messageCounter.value = it
        }
    }

    fun showWidget() {
        _showEvent.value = Unit
    }
}

private const val SHARED_PREFERENCE_NAME = "SabycomDemo"
private const val UUID_KEY = "uuid"