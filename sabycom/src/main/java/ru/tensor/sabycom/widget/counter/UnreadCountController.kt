package ru.tensor.sabycom.widget.counter

import android.os.Handler
import android.os.Looper
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author ma.kolpakov
 */
internal class UnreadCountController(private val repository: Repository) : IUnreadCountController {
    private val handler = Handler(Looper.getMainLooper())
    override var callback: UnreadCounterCallback? = null

    override fun requestCount() {
        repository.getUnreadMessageCount {
            updateCount(it)
        }
    }

    override fun updateCount(newCount: Int) {
        handler.post {
            callback?.updateCount(newCount)
        }
    }

}