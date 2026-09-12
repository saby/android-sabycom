package ru.tensor.sabycom.push.builder.binder

import android.content.Context

/**
 * Биндер для создания и привязки представления (view) с моделью уведомления.
 *
 * @author am.boldinov
 */
internal interface NotificationBinder<DATA, NOTIFICATION> {

    /**
     * Создает экземпляр представления уведомления.
     * @param context контекст приложения или активности в зависимости от места вызова
     *
     * @return view для отображения уведомления.
     */
    fun create(context: Context): NOTIFICATION

    /**
     * Устанавливает данные по уведомлению во view.
     * @param view представление для отображения уведомления
     * @param data данные для биндинга во view
     */
    fun bind(view: NOTIFICATION, data: DATA)
}