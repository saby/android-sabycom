package ru.tensor.sabycomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycomdemo.databinding.ActivitySabycomDemoBinding

/**
 * @author ma.kolpakov
 */
class SabycomDemoActivity : AppCompatActivity() {
    private val viewModel: DemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySabycomDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.showSabycom.setOnClickListener {
            Sabycom.show(this)
        }

        viewModel.messageCounter.observe(this) {
            binding.counter.text = it.toString()
        }

    }

}
