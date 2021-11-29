package ru.tensor.sabycom.widget

import android.content.Context
import ru.tensor.sabycom.data.UrlUtil
import java.lang.Exception

/**
 * Метод достает тип стенда из локального хранилища если приложение из пакета [ru.tensor] это необходимо только для тестирования
 *
 * @author ma.kolpakov
 */
internal fun setStand(context: Context) {
    val appPackage = context.applicationContext.applicationInfo.name
    if (!appPackage.startsWith("ru.tensor")) return
    val sharedPreferences = context.getSharedPreferences(SABYCOM_STAND_PREFS, Context.MODE_PRIVATE)
    val standString = sharedPreferences.getString(CURRENT_STAND_KEY, "")!!

    if (standString.isEmpty()) return
    try {
        UrlUtil.currentStand = UrlUtil.StandPrefix.valueOf(standString.uppercase())
    } catch (e: Exception) {
        //ignore
    }
}

private const val SABYCOM_STAND_PREFS = "SABYCOM_STAND_PREFS"
private const val CURRENT_STAND_KEY = "CURRENT_STAND_KEY"