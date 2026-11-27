package ru.tensor.sabycom.widget

import androidx.appcompat.app.AppCompatActivity
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.widget.repository.Repository
import java.util.*

/**
 * @author ma.kolpakov
 */
internal class SabycomFeature(
    apiKey: String,
    private val repository: Repository
) {
    internal var onClose: (() -> Unit)? = null

    init {
        repository.setApiKey(apiKey)
    }

    fun show(activity: AppCompatActivity) {
        activity.startActivity(SabycomActivity.createIntent(activity))
    }

    fun hide() {
        onClose?.invoke()
    }

    fun registerUser(userData: UserData) {
        repository.registerUser(userData)
        repository.registerAnonymousUserId(null)
    }

    fun registerAnonymousUser() {
        var anonymousUserId = repository.getAnonymousUserId()
        if (anonymousUserId == null) {
            val newId = UUID.randomUUID()
            anonymousUserId = newId.toString()
            repository.registerAnonymousUserId(anonymousUserId)
        }

        repository.registerUser(UserData(UUID.fromString(anonymousUserId)))
    }
}
