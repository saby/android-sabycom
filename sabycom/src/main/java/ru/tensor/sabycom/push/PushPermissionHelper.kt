package ru.tensor.sabycom.push

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.UiThread

/**
 * Хелпер для получения разрешения на показ пуш-уведомлений.
 *
 * @author am.boldinov
 */
@UiThread
object PushPermissionHelper {

    @Suppress("MemberVisibilityCanBePrivate")
    @JvmField
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    private var requested = false

    /**
     * Отправляет пользователю запрос на получение разрешений на показ пуш-уведомлений.
     * По умолчанию показывает запрос только 1 раз.
     */
    @JvmStatic
    fun requestNotificationPermission(activity: Activity, oneTime: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && (!requested || !oneTime)) {
            requested = true
            if (permissions.isNotEmpty()) {
                val candidates = ArrayList<String>(permissions.size)
                permissions.forEach { permission ->
                    if (activity.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
                        && (!activity.shouldShowRequestPermissionRationale(permission) || !oneTime)
                    ) {
                        candidates.add(permission)
                    }
                }
                if (candidates.isNotEmpty()) {
                    activity.requestPermissions(candidates.toTypedArray(), 1)
                }
            }
        }
    }
}