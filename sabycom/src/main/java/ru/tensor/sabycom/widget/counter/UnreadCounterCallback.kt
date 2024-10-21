package ru.tensor.sabycom.widget.counter

import androidx.annotation.MainThread

/**
 * Коллбек обновления непрочитанных сообщений
 *
 * @author ma.kolpakov
 */

@MainThread
fun interface UnreadCounterCallback {
    /**@SelfDocumented**/
    fun updateCount(count: Int)
}