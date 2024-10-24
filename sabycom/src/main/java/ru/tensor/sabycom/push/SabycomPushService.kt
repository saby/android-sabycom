package ru.tensor.sabycom.push

import androidx.annotation.AnyThread

/**
 * @author am.boldinov
 */
@AnyThread
interface SabycomPushService {

    fun isSabycomPushNotification(payload: Map<String, String>): Boolean

    fun handlePushNotification(payload: Map<String, String>)

    fun sendToken(token: String)
}