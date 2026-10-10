package ru.tensor.sabycom.widget

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity

/**
 * @author ma.kolpakov
 */
internal class SabycomActivity : AppCompatActivity() {
    private val viewModel: SabycomActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.openEvent.observe(this) { state ->
            if (supportFragmentManager.findFragmentByTag(SABYCOM_DIALOG_FRAGMENT_TAG) != null) return@observe
            SabycomDialog.newInstance(state.url, state.userData)
                .show(supportFragmentManager, SABYCOM_DIALOG_FRAGMENT_TAG)
        }

        viewModel.closeEvent.observe(this) {
            onBackPressed()
        }
    }

    internal companion object {
        /**
         * Метод предоставляет интент для открытия активити с виджетом онлайн консультации
         */
        fun createIntent(context: Context): Intent {
            return Intent(context, SabycomActivity::class.java)
        }

        private const val SABYCOM_DIALOG_FRAGMENT_TAG = "SabycomDialogFragmentTag"
    }
}
