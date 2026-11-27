package ru.tensor.sabycomdemo.push

import android.content.Context
import com.huawei.agconnect.AGConnectInstance
import com.huawei.agconnect.AGConnectOptionsBuilder
import com.huawei.hmf.tasks.TaskExecutors
import com.huawei.hmf.tasks.Tasks
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.push.HmsMessaging
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.push.ServiceType

/**
 * Реализация подписчика на получение новых push-уведомлений от Huawei Cloud Messaging.
 *
 * @author am.boldinov
 */
internal object DemoPushMessagingSubscriber : PushMessagingSubscriber {

    override fun subscribe(context: Context) {
        Tasks.callInBackground {
            val appId = getAppId(context)
            HmsInstanceId.getInstance(context).getToken(appId, HmsMessaging.DEFAULT_TOKEN_SCOPE)
        }.addOnSuccessListener(TaskExecutors.immediate()) { token ->
            if (!token.isNullOrEmpty()) {
                Sabycom.sendToken(token, ServiceType.HUAWEI)
            }
        }.addOnFailureListener(TaskExecutors.immediate()) { exception ->
            exception.printStackTrace()
        }
    }

    private fun getAppId(context: Context): String {
        val options = AGConnectInstance.getInstance()?.options ?: AGConnectOptionsBuilder().build(context)
        return options.getString("/client/app_id") ?: throw IllegalStateException("Please check agconnect-services.json file availability in your app project")
    }
}