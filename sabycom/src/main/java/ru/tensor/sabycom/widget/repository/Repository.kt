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
        if (localRepository.getUserData()!=null){
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

    fun getUserData() = requireNotNull(localRepository.getUserData()) { USER_NOT_REGISTER_ERROR }

    fun getApiKey() = requireNotNull(localRepository.getApiKey()) { NOT_INIT_ERROR }

    private fun syncUserData() {
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

    private companion object{
        private const val USER_NOT_REGISTER_ERROR = "Before using Sabycom, it is necessary to register user " +
                " [Sabycom.registerUser(userData: UserData)] or [Sabycom.registerAnonymousUser()] "
    }
}