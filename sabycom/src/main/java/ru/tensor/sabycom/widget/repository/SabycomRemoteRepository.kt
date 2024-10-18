package ru.tensor.sabycom.widget.repository

import org.json.JSONObject
import ru.tensor.sabycom.data.ApiClient
import ru.tensor.sabycom.data.UserData
import java.util.concurrent.Executors

/**
 * TODO 29.09.21 переработать кеширование, сделано для тестирования работы
 *
 * @author am.boldinov
 */
internal class SabycomRemoteRepository : RemoteRepository {

    private val executor by lazy { Executors.newSingleThreadExecutor() }
    private var registerData: RegisterData? = null

    override fun sendPushToken(token: String) {
        executor.submit {
            registerData = (registerData?.let {
                RegisterData(it.user, it.apiKey, it.token)
            } ?: RegisterData(token = token)).apply {
                if (user != null && apiKey != null) {
                    performRegisterSync(user, apiKey, token)
                }
            }
        }
    }

    override fun registerUser(user: UserData, apiKey: String) {
        executor.submit {
            registerData = registerData?.let {
                RegisterData(user, apiKey, it.token)
            } ?: RegisterData(user, apiKey)
            performRegisterSync(user, apiKey, registerData?.token)
        }
    }

    private fun performRegisterSync(user: UserData, apiKey: String, token: String?) {
        val data = JSONObject().apply {
            put("id", user.id.toString())
            put("service_id", apiKey)
            put("name", user.name)
            put("surname", user.surname)
            put("email", user.email)
            put("phone", user.phone)
            put("push_token", token)
            put("push_os", "android")
        }
        ApiClient.put("externalUser/${user.id}/$apiKey", data, object : ApiClient.ResultCallback {
            override fun onSuccess(result: JSONObject) {

            }

            override fun onFailure(code: Int, errorBody: JSONObject) {

            }
        })
    }

    private class RegisterData(
        val user: UserData? = null,
        val apiKey: String? = null,
        val token: String? = null
    )
}