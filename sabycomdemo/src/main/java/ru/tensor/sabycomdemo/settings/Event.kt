package ru.tensor.sabycomdemo.settings

/**
 * Событие для LiveData которое обрабатывается один раз.
 *
 * @author ma.kolpakov
 */
internal open class Event<out T>(private val content: T) {

    private var isHandled = false

    /**
     * Получить контент. Если событие уже было обработано вернется null
     */
    fun getContentIfNotHandled(): T? {
        return if (isHandled) {
            null
        } else {
            isHandled = true
            content
        }
    }
}
