package ru.tensor.sabycom.push.manager

/**
 * Интерфейс для отправки событий по действиям над уведомлениями.
 * Необходимо использовать в основном при обработке действий
 * пользователя (смахивание, нажатие на уведомление и пр.)
 *
 * @author am.boldinov
 */
internal interface NotificationActionDispatcher {

    /**
     * Посылает событие о необходимости удалить уведомление.
     * @param tag тэг уведомления, под которым оно было опубликовано
     * @param id идентификатор уведомления
     */
    fun dispatchOnCancel(tag: String, id: Int)

    /**
     * Посылает событие о необходимости удалить все опубликованные уведомления.
     */
    fun dispatchOnCancelAll()
}