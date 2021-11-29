package ru.tensor.sabycomdemo.demo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.widget.counter.UnreadCounterCallback

/**
 * Вью модель экрана с демонстрацией виджета "оналйн консультант"
 *
 * @author ma.kolpakov
 */
class DemoViewModel(application: Application) : AndroidViewModel(application) {
    private val messageCounterLiveData = MutableLiveData(0)
    val messageCounter: LiveData<Int> = messageCounterLiveData

    init {
        Sabycom.unreadConversationCount(object : UnreadCounterCallback {
            override fun updateCount(count: Int) {
                messageCounterLiveData.value = count
            }
        })
    }

}
