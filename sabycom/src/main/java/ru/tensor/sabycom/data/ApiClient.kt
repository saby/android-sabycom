package ru.tensor.sabycom.data

import androidx.annotation.WorkerThread
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

/**
 * @author am.boldinov
 */
@WorkerThread
internal object ApiClient {

    private val API_URL = UrlUtil.HOST_URL.plus("/service/restapi/")
    private const val CONNECT_TIMEOUT = 10_000 // 10 sec
    private const val READ_TIMEOUT = CONNECT_TIMEOUT

    interface ResultCallback {

        fun onSuccess(result: JSONObject)

        fun onFailure(code: Int, errorBody: JSONObject)

    }

    fun put(path: String, params: JSONObject, callback: ResultCallback) {
        getConnection(API_URL.plus(path), "PUT").apply {
            doOutput = true
            outputStream.bufferedWriter().use {
                it.write(params.toString())
                it.flush()
            }
            handleResponse(callback)
        }
    }

    fun get(path: String, callback: ResultCallback) {
        getConnection(API_URL.plus(path), "GET").apply {
            handleResponse(callback)
        }
    }

    private fun HttpsURLConnection.handleResponse(callback: ResultCallback) {
        try {
            val response = inputStream.bufferedReader().use {
                JSONObject(it.readText())
            }
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                callback.onSuccess(response)
            } else {
                callback.onFailure(responseCode, response)
            }
        } catch (e: IOException) {
            val errorBody = errorStream.bufferedReader().use {
                JSONObject(it.readText())
            }
            callback.onFailure(responseCode, errorBody)
        } finally {
            disconnect()
        }
    }


    private fun getConnection(url: String, type: String): HttpsURLConnection {
        return (URL(url).openConnection() as HttpsURLConnection).apply {
            requestMethod = type
            setRequestProperty("Content-Type", "application/json")
            setRequestProperty("Accept", "application/json")
            connectTimeout = CONNECT_TIMEOUT
            readTimeout = READ_TIMEOUT
        }
    }
}

