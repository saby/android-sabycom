package ru.tensor.sabycom.push

/**
 * Типы мобильных служб, поддерживаемых виджетом.
 * Необходимо использовать для подписки на уведомления в паре с токеном регистрации.
 *
 * @author am.boldinov
 */
enum class ServiceType(internal val value: String) {
    /**
     * Google Mobile Services (GMS)
     */
    GOOGLE("android_fcm"),

    /**
     * Huawei Mobile Services (HMS)
     */
    HUAWEI("android_hcm")
}
