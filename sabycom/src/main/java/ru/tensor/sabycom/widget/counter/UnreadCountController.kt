package ru.tensor.sabycom.widget.counter

import android.os.Handler
import android.os.Looper
import ru.tensor.sabycom.widget.repository.Repository
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * @author ma.kolpakov
 */
internal class UnreadCountController(private val repository: Repository) : IUnreadCountController {

    private val handler = Handler(Looper.getMainLooper())
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private var feature: ScheduledFuture<*>? = null
    private var task = { requestCount() }

    override var callback: UnreadCounterCallback? = null

    override fun requestCount() {
        feature?.cancel(true)
        repository.getUnreadMessageCount {
            feature = scheduler.schedule(task, TIME_TO_REQUEST_COUNT, TIME_UNIT)
            updateCount(it)
        }
    }

    override fun updateCount(newCount: Int) {
        handler.post {
            callback?.updateCount(newCount)
        }
    }

    private companion object{
        const val TIME_TO_REQUEST_COUNT = 60L
        val TIME_UNIT = TimeUnit.SECONDS
    }

}