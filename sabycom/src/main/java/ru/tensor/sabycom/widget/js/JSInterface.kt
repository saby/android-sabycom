package ru.tensor.sabycom.widget.js

import android.webkit.JavascriptInterface
import androidx.annotation.WorkerThread
import org.json.JSONObject
import ru.tensor.sabycom.widget.counter.IUnreadCountController

/**
 * Интерфейс взаимодействия с JS кодом веб виджета. Обрабатывает события от виджета.
 * @author ma.kolpakov
 */
internal class JSInterface(
    private val countController: IUnreadCountController,
    private val onCloseListener: OnJsCloseListener
) {

    @JavascriptInterface
    fun postMessage(data: String, targetOrigin: String) {
        val dataJson = JSONObject(data)
        when (dataJson.getString(ACTION)) {
            // Нажатие на кнопку зкарыть внутри виджета
            ACTION_CLOSE -> {
                onCloseListener.onClose()
            }
            // Изменение непрочитанных сообзений
            ACTION_UPDATE_UNREAD_MESSAGES -> {
                countController.updateCount(dataJson.getInt(VALUE))
            }
            else -> {
                //do nothing
            }
        }
    }

    private companion object {
        const val ACTION_CLOSE = "toggleWindow"
        const val ACTION_UPDATE_UNREAD_MESSAGES = "unreadChange"
        const val ACTION = "action"
        const val VALUE = "value"
    }

    fun interface OnJsCloseListener {
        @WorkerThread
        fun onClose()
    }
}