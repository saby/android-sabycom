package ru.tensor.sabycom.widget.js

import android.util.Log
import android.webkit.JavascriptInterface
import androidx.annotation.WorkerThread
import org.json.JSONException
import org.json.JSONObject
import ru.tensor.sabycom.widget.counter.IUnreadCountController

/**
 * Интерфейс взаимодействия с JS кодом веб виджета. Обрабатывает события от виджета.
 * @author ma.kolpakov
 */
internal class JSInterface(
    private val countController: IUnreadCountController,
    private val onCloseListener: OnJsCloseListener,
    private val onContentScrollListener: OnContentScrollListener
) {

    @JavascriptInterface
    fun postMessage(data: String, targetOrigin: String) {
        val dataJson = JSONObject(data)
        when (dataJson.getString(ACTION)) {
            // Нажатие на кнопку закрыть внутри виджета
            ACTION_CLOSE -> {
                onCloseListener.onClose()
            }
            // Изменение непрочитанных сообщений
            ACTION_UPDATE_UNREAD_MESSAGES -> {
                countController.updateCount(dataJson.getInt(VALUE))
            }
            ACTION_SCROLL_CHANGED -> {
                try {
                    val value = dataJson
                        .getJSONObject(VALUE)
                        .getJSONObject(SCROLL_DATA_KEY)
                        .getBoolean(SCROLLED_OT_TOP_KEY)
                    onContentScrollListener.onScrollChange(!value)
                } catch (e: JSONException) {
                    Log.d(LOG_TAG, "Invalid scrolling data", e)
                }
            }
            else -> {
                //do nothing
            }
        }
    }

    private companion object {
        const val ACTION_CLOSE = "toggleWindow"
        const val ACTION_SCROLL_CHANGED = "scrollChanged"
        const val ACTION_UPDATE_UNREAD_MESSAGES = "unreadChange"
        const val ACTION = "action"
        const val VALUE = "value"
        const val SCROLL_DATA_KEY = "scrollData"
        const val SCROLLED_OT_TOP_KEY = "isScrolledToTop"
        const val LOG_TAG = "WEB_VIEW_JS_BRIDGE"
    }

    fun interface OnJsCloseListener {
        @WorkerThread
        fun onClose()
    }

    fun interface OnContentScrollListener {
        /**
         *  Вызывается при изменении скролла в вебвью.
         *  @param isContentScrolling  признак того скролится ли в данный момент контент.
         */
        @WorkerThread
        fun onScrollChange(isContentScrolling: Boolean)
    }
}