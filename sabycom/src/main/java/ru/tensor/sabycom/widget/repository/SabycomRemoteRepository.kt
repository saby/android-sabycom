package ru.tensor.sabycom.widget.repository

import android.util.Log
import org.json.JSONObject
import ru.tensor.sabycom.data.ApiClient
import ru.tensor.sabycom.data.UserData
import java.util.concurrent.Executors

/**
 * @author am.boldinov
 */
internal class SabycomRemoteRepository : RemoteRepository {

    private val executor by lazy { Executors.newSingleThreadExecutor() }

    override fun getUnreadMessageCount(
        apiKey: String,
        userData: UserData,
        callback: (Int) -> Unit
    ) {
        executor.submit {
            val path = "externalUser/${userData.id}/${apiKey}/unread/${apiKey}"
            ApiClient.get(
                path,
                object : ApiClient.ResultCallback {
                    override fun onSuccess(result: JSONObject) {
                        callback(result.getJSONObject("result").getInt("count"))
                    }

                    override fun onFailure(code: Int, errorBody: JSONObject) {
                        Log.d(
                            LOG_TAG,
                            "Error in get request with path[$path]. Error code [$code]. Error message [$errorBody]"
                        )
                    }
                })
        }
    }

    override fun performRegisterSync(apiKey: String, userData: UserData, token: String?, isUnsubscribe: Boolean) {
        executor.submit {
            val data = JSONObject().apply {
                put("id", userData.id.toString())
                put("service_id", apiKey)
                put("name", userData.name)
                put("surname", userData.surname)
                put("email", userData.email)
                put("phone", userData.phone)
                put("push_token", token)
                put("push_os", "android_fcm")
                if (isUnsubscribe) {
                    put("push_unsubscribe", true)
                }
            }
            val path = "externalUser/${userData.id}/$apiKey"
            ApiClient.put(
                path,
                data,
                object : ApiClient.ResultCallback {
                    override fun onSuccess(result: JSONObject) {
                        Log.d(
                            LOG_TAG,
                            "Put request completed successfully with path path[$path]"
                        )
                    }

                    override fun onFailure(code: Int, errorBody: JSONObject) {
                        Log.d(
                            LOG_TAG,
                            "Error in put request with path[$path]. Error code [$code]. Error message [$errorBody]"
                        )
                    }
                })
        }
    }

    private companion object {
        const val LOG_TAG = "SabycomRemoteRepository"
    }
}