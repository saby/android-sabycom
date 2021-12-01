package ru.tensor.sabycomdemo.settings

/**
 * Событие для LiveData которое обрабатывается один раз.
 *
 * @author ma.kolpakov
 */
internal class Event<out T>(private val content: T) {

    private var isHandled = false

    /**
     * Получить контент. Если событие еще не было  обработано
     */
    fun handleEvent(handler: (T) -> Unit) {
        if (!isHandled) {
            isHandled = true
            handler(content)
        }
    }
}
