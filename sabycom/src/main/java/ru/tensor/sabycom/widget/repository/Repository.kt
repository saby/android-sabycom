package ru.tensor.sabycom.widget.repository

import ru.tensor.sabycom.Sabycom.NOT_INIT_ERROR
import ru.tensor.sabycom.data.UserData

/**
 * @author ma.kolpakov
 */
internal class Repository(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) {
    fun sendPushToken(token: String) {
        localRepository.savePushToken(token)
        if (getUserData() != null) {
            syncUserData()
        }
    }

    fun registerUser(userData: UserData) {
        localRepository.saveUser(userData)
        syncUserData()
    }

    fun setApiKey(apiKey: String) {
        localRepository.saveApiKey(apiKey)
    }

    fun getUserData() = localRepository.getUserData()

    fun requireUserData() = requireNotNull(localRepository.getUserData()) { USER_NOT_REGISTER_ERROR }

    fun requireApiKey() = requireNotNull(localRepository.getApiKey()) { NOT_INIT_ERROR }

    private fun syncUserData() {
        remoteRepository.performRegisterSync(
            requireApiKey(),
            requireUserData(),
            localRepository.getPushToken()
        )
    }

    fun registerAnonymousUserId(id: String?) {
        localRepository.saveAnonymousUserId(id)
    }

    fun getAnonymousUserId(): String? {
        return localRepository.getAnonymousUserId()
    }

    fun getUnreadMessageCount(callback: (Int) -> Unit) {
        remoteRepository.getUnreadMessageCount(
            requireApiKey(),
            requireUserData(),
            callback
        )
    }

    fun unregisterUser() {
        remoteRepository.performRegisterSync(
            requireApiKey(),
            requireUserData(),
            localRepository.getPushToken(),
            true
        )
        localRepository.saveUser(null)
    }

    private companion object {
        private const val USER_NOT_REGISTER_ERROR = "Before using Sabycom, it is necessary to register a user " +
                "Sabycom.registerUser(userData: UserData) or Sabycom.registerAnonymousUser()"
    }
}