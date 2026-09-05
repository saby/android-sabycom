package ru.tensor.sabycom.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.tensor.sabycom.R
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.databinding.SabycomDialogBinding
import ru.tensor.sabycom.widget.js.JSInterface


/**
 * @author ma.kolpakov
 */
internal class SabycomDialog : BottomSheetDialogFragment() {
    private lateinit var binding: SabycomDialogBinding
    private lateinit var url: String
    private lateinit var userData: UserData
    private val viewModel: SabycomActivityViewModel by activityViewModels()

    companion object {
        fun newInstance(url: String, userData: UserData): SabycomDialog {
            return SabycomDialog().apply {
                arguments = Bundle()
                this.url = url
                this.userData = userData
                requireArguments().putString(ARG_URL, url)
                requireArguments().putParcelable(ARG_USER_DATA, userData)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(requireArguments()) {
            url = getString(ARG_URL)!!
            userData = getParcelable(ARG_USER_DATA)!!
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                it.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                    height = CoordinatorLayout.LayoutParams.MATCH_PARENT
                }
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
            val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet as View)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        }
        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // TODO: 17.09.2021 перейти на использование WindowInsets 
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        binding = SabycomDialogBinding.inflate(inflater)

        prepareWebView(binding.webView)

        binding.webView.loadUrl(url, createHeaders(userData))

        return binding.root
    }

    private fun createHeaders(userData: UserData): MutableMap<String, String> {
        val headers = mutableMapOf("id" to userData.id.toString())
        headers.putIfNotNull("name", userData.name)
        headers.putIfNotNull("surname", userData.surname)
        headers.putIfNotNull("email", userData.email)
        headers.putIfNotNull("phone", userData.phone)
        return headers
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        requireActivity().onBackPressed()
    }

    // Можно использовать JavaScript так как мы загружаем только наш веб-виджет
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun prepareWebView(webView: WebView): WebView {
        webView.addJavascriptInterface(
            JSInterface(Sabycom.countController) {
                requireActivity().runOnUiThread {
                    viewModel.hide()
                }
            },
            "mobileParent"
        )

        webView.settings.javaScriptEnabled = true
        return webView
    }

    private fun MutableMap<String, String>.putIfNotNull(key: String, value: String?) {
        if (value != null) this[key] = value
    }
}

private const val ARG_URL = "URL"
private const val ARG_USER_DATA = "USER_DATA"