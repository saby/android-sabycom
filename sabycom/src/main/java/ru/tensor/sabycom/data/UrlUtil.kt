package ru.tensor.sabycom.data

import android.util.Base64
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import kotlin.jvm.Throws

/**
 * @author am.boldinov
 */
internal object UrlUtil {
    internal var currentHost: Host = Host.PRE_TEST
    private const val HOST_URL_TEMPLATE = "https://%sconsultant.sbis.ru" // TODO 29.09.2021 настраиваемый gradle url
    val HOST_URL: String get() = HOST_URL_TEMPLATE.format(currentHost.prefix)

    fun buildWidgetUrl(userId: String, apiKey: String): String {
        val params = JSONObject().apply {
            put("ep", JSONObject().apply {
                put("id", userId)
                put("service_id", apiKey)
            })
        }
        return HOST_URL.plus("/consultant/$apiKey/?p=${params.encodeParams()}")
    }

    /**
     * Форматирует ссылки на внутренние ресурсы, которые приходят без хоста.
     */
    fun formatUrlWithHost(url: String): String {
        return HOST_URL.plus(if (url.startsWith("/")) url else "/$url")
    }

    @Throws(UnsupportedEncodingException::class)
    private fun JSONObject.encodeParams(): String {
        val plainTextBytes = toString().toByteArray(Charsets.UTF_8)
        val base64String = Base64.encodeToString(plainTextBytes, 0)
        return URLEncoder.encode(base64String, Charsets.UTF_8.displayName())
    }

    enum class Host(val prefix: String) {
        PROD(""),
        FIX("fix-"),
        TEST("test-"),
        PRE_TEST("pre-test-"),
        DEV("dev-"),
    }
}