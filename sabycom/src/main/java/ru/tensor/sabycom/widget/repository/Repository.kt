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
        syncUserData()
    }

    fun registerUser(userData: UserData) {
        localRepository.saveUser(userData)
        syncUserData()
    }

    fun setApiKey(apiKey: String) {
        localRepository.saveApiKey(apiKey)
    }

    fun getUserData() = requireNotNull(localRepository.getUserData()) { NOT_INIT_ERROR }

    fun getApiKey() = requireNotNull(localRepository.getApiKey()) { NOT_INIT_ERROR }

    fun syncUserData() {
        remoteRepository.performRegisterSync(
            getApiKey(),
            getUserData(),
            localRepository.getPushToken()
        )
    }

    fun getUnreadMessageCount(callback: (Int) -> Unit) {
        remoteRepository.getUnreadMessageCount(
            getApiKey(),
            getUserData(),
            callback
        )
    }

}