package ru.tensor.sabycomdemo

import android.app.Application
import com.google.firebase.messaging.FirebaseMessaging
import ru.tensor.sabycom.Sabycom

/**
 * @author ma.kolpakov
 */
class SabycomApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Sabycom.initialize(applicationContext, "5cf633f3-481a-40e7-b254-a60200c87dc4")
        refreshToken()
    }

    private fun refreshToken() {
        FirebaseMessaging.getInstance()
            .token
            .addOnSuccessListener { token ->
                Sabycom.sendToken(token)
            }.addOnFailureListener {
                it.printStackTrace()
            }
    }
}