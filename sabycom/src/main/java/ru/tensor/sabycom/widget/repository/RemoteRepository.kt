package ru.tensor.sabycom.widget.repository

import ru.tensor.sabycom.data.UserData

/**
 * @author am.boldinov
 */
internal interface RemoteRepository {

    fun sendPushToken(token: String)

    fun registerUser(user: UserData, apiKey: String)
}