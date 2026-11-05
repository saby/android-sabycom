package ru.tensor.sabycom.widget

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.tensor.sabycom.data.UrlUtil
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.repository.RemoteRepository

/**
 * @author ma.kolpakov
 */
internal class SabycomFeature(
    private val apiKey: String,
    private val repository: RemoteRepository
) {

    private val hideEvent = MutableLiveData<Unit>()
    private var userData: UserData? = null

    fun show(activity: AppCompatActivity) {
        userData?.let { user ->
            val dialog = SabycomDialog.newInstance(
                UrlUtil.buildWidgetUrl(userId = user.id.toString(), apiKey = apiKey),
                checkNotNull(userData) { NO_USER_DATA_ERROR })

            hideEvent.observeOnce(activity) {
                dialog.dismiss()
            }

            dialog.show(activity.supportFragmentManager, SABYCOM_TAG)
        }
    }

    fun hide() {
        hideEvent.value = Unit
    }

    fun registerUser(userData: UserData) {
        this.userData = userData
        repository.registerUser(userData, apiKey)
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}

private const val SABYCOM_TAG = "Sabycom"
private const val NO_USER_DATA_ERROR =
    "Before showing widget, you need to register the user by calling method [Sabycom.registerUser(<user data>)]"