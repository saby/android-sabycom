package ru.tensor.sabycom.widget

import android.content.Context
import android.util.Log
import ru.tensor.sabycom.data.UrlUtil
import java.lang.Exception

/**
 * Если приложение из пакета [ru.tensor], метод достает тип стенда из локального хранилища. Это необходимо только для тестирования.
 *
 * @author ma.kolpakov
 */
internal fun setHost(context: Context) {
    val appPackage = context.packageName
    if (!appPackage.startsWith("ru.tensor")) return
    val sharedPreferences = context.getSharedPreferences(SABYCOM_HOST_PREFS, Context.MODE_PRIVATE)
    val hostString = sharedPreferences.getString(CURRENT_HOST_KEY, "")!!

    if (hostString.isEmpty()) return
    try {
        UrlUtil.currentHost = UrlUtil.Host.valueOf(hostString.uppercase())
    } catch (e: Exception) {
        Log.d(LOG_TAG,"Unresolved host prefix $hostString")
    }
}

private const val SABYCOM_HOST_PREFS = "SABYCOM_HOST_PREFS"
private const val CURRENT_HOST_KEY = "CURRENT_HOST_KEY"
private const val LOG_TAG = "SABYCOM_DEMO"