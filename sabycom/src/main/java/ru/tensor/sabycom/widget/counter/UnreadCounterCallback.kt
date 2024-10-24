package ru.tensor.sabycom.widget.counter

import androidx.annotation.MainThread

/**
 * Коллбек обновления непрочитанных сообщений
 *
 * @author ma.kolpakov
 */

@MainThread
interface UnreadCounterCallback {
    /**@SelfDocumented**/
    fun updateCount(count: Int)
}