package ru.tensor.sabycom.widget.repository

import ru.tensor.sabycom.data.UserData

/**
 * @author am.boldinov
 */
internal interface RemoteRepository {

    fun performRegisterSync(apiKey: String, userData: UserData, token: String?, isUnsubscribe : Boolean = false)

    fun getUnreadMessageCount(apiKey: String, userData: UserData, callback: (Int) -> Unit)

}