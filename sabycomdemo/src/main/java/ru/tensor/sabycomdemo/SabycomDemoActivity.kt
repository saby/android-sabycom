package ru.tensor.sabycomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import ru.tensor.sabycom.push.PushPermissionHelper
import ru.tensor.sabycomdemo.databinding.ActivitySabycomDemoBinding

/**
 * @author ma.kolpakov
 */
class SabycomDemoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySabycomDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        PushPermissionHelper.requestNotificationPermission(this)
    }
}
