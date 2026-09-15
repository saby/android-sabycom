package ru.tensor.sabycomdemo

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.counter.UnreadCounterCallback
import java.util.UUID

/**
 * @author ma.kolpakov
 */
class DemoViewModel(application: Application) : AndroidViewModel(application) {
    private val messageCounterLiveData = MutableLiveData(0)
    val messageCounter: LiveData<Int> = messageCounterLiveData

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

        Sabycom.unreadConversationCount(object : UnreadCounterCallback {
            override fun updateCount(count: Int) {
                messageCounterLiveData.value = count
            }
        })
    }

}

private const val SHARED_PREFERENCE_NAME = "SabycomDemo"
private const val UUID_KEY = "uuid"