package ru.tensor.sabycom.widget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UrlUtil
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.network.NetworkAvailable
import ru.tensor.sabycom.widget.network.NetworkAvailableImpl
import ru.tensor.sabycom.widget.repository.Repository

/**
 * @author ma.kolpakov
 */
internal class SabycomActivityViewModel(
    application: Application,
    repository: Repository = Sabycom.repository,
    private val networkAvailable: NetworkAvailable = NetworkAvailableImpl()
) :
    AndroidViewModel(application), NetworkAvailable by networkAvailable {

    constructor(application: Application) : this(application, Sabycom.repository)

    private val _openEvent = MutableLiveData<OpenWidgetData>()
    val openEvent: LiveData<OpenWidgetData> = _openEvent

    private val _closeEvent = MutableLiveData<Unit>()
    val closeEvent: LiveData<Unit> = _closeEvent

    private val _pageReady = MutableLiveData<Unit>()
    val pageReady: LiveData<Unit> = _pageReady

    init {
        _openEvent.value = OpenWidgetData(
            UrlUtil.buildWidgetUrl(
                userId = repository.requireUserData().id.toString(),
                apiKey = repository.requireApiKey()
            ),
            repository.requireUserData(),
            repository.requireApiKey()
        )
        Sabycom.sabycomFeature?.onClose = {
            hide()
        }

        networkAvailable.bind(application)
    }


    override fun onCleared() {
        Sabycom.sabycomFeature?.onClose = null
        networkAvailable.unbind()
    }

    fun hide() {
        _closeEvent.postValue(Unit)
    }

    fun showWebView() {
        _pageReady.postValue(Unit)
    }

    internal data class OpenWidgetData(val url: String, val userData: UserData, val channel: String)
}
