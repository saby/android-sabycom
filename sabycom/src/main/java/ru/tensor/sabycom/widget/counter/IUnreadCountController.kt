package ru.tensor.sabycom.widget.counter

/**
 * Контроллер количества не прочитанных сообщений
 * @author ma.kolpakov
 */
internal interface IUnreadCountController {
    /**
     * Колбек вызывается при изменении количества не прочитанных сообщений
     */
    var callback: UnreadCounterCallback?

    /**
     * Запросить количество не прочитанных сообщений из облака
     */
    fun requestCount()

    /**
     * Обновить колисество не прочитанных сообщений из иного источника
     */
    fun updateCount(newCount: Int)
}