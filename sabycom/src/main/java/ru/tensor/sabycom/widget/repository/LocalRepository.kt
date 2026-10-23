package ru.tensor.sabycom.widget.repository

import ru.tensor.sabycom.data.UserData

/**
 * @author ma.kolpakov
 */
internal interface LocalRepository {
    fun savePushToken(token: String)
    fun getPushToken(): String?
    fun saveUser(user: UserData?)
    fun saveApiKey(apiKey: String)
    fun getApiKey(): String?
    fun getUserData(): UserData?
    fun saveAnonymousUserId(id: String?)
    fun getAnonymousUserId(): String?
}