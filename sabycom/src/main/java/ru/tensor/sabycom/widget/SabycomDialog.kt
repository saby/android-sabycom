package ru.tensor.sabycom.widget

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.tensor.sabycom.R
import ru.tensor.sabycom.Sabycom
import ru.tensor.sabycom.data.UserData
import ru.tensor.sabycom.databinding.SabycomDialogBinding
import ru.tensor.sabycom.push.util.attachNotificationLocker
import ru.tensor.sabycom.widget.js.JSInterface
import android.content.Intent
import im.delight.android.webview.AdvancedWebView
import ru.tensor.sabycom.widget.webview.WebViewInteractor


/**
 * @author ma.kolpakov
 */
internal class SabycomDialog : BottomSheetDialogFragment() {
    private var binding: SabycomDialogBinding? = null
    private lateinit var url: String
    private lateinit var userData: UserData
    private val viewModel: SabycomActivityViewModel by activityViewModels()
    private var isContentScrolling = true
    private lateinit var webViewInteractor: WebViewInteractor

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
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SabycomFullScreenDialog)
        attachNotificationLocker(Sabycom.notificationLocker)
        with(requireArguments()) {
            url = getString(ARG_URL)!!
            userData = getParcelable(ARG_USER_DATA)!!
        }

        webViewInteractor = WebViewInteractor(this) {
            showError()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        bottomSheetDialog.setOnShowListener { dialog: DialogInterface ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                it.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                    height = CoordinatorLayout.LayoutParams.MATCH_PARENT
                }

                behaviour.addBottomSheetCallback(object :
                    BottomSheetBehavior.BottomSheetCallback() {
                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING && isContentScrolling) {
                            behaviour.setState(BottomSheetBehavior.STATE_EXPANDED)
                        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                            dismiss()
                        }
                    }

                    override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
                })

                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }

        }
        return bottomSheetDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SabycomDialogBinding.inflate(inflater).apply {
            prepareWebView(webView)
            webView.loadUrl(url)
        }

        return binding!!.root
    }

    @SuppressLint("NewApi")
    override fun onResume() {
        super.onResume()
        binding?.webView?.onResume()
    }

    @SuppressLint("NewApi")
    override fun onPause() {
        binding?.webView?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding?.webView?.onDestroy()
        binding = null
        super.onDestroyView()
    }

    /**
     * Требуется для отправки файлов с помощью AdvancedWebView
     * https://github.com/delight-im/Android-AdvancedWebView#with-fragments-from-the-support-library-androidsupportv4appfragment
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        binding?.webView?.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        requireActivity().onBackPressed()
    }

    private fun showError() {
        binding?.apply {
            webView.visibility = View.GONE
            errorMessage.visibility = View.VISIBLE
            isContentScrolling = false
        }
    }

    // Можно использовать JavaScript так как мы загружаем только наш веб-виджет
    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    private fun prepareWebView(webView: AdvancedWebView) {
        webView.setListener(requireActivity(), webViewInteractor)
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.addJavascriptInterface(
            JSInterface(Sabycom.countController, {
                requireActivity().runOnUiThread {
                    viewModel.hide()
                }
            }, {
                isContentScrolling = it
            }),
            "mobileParent"
        )
    }
}

private const val ARG_URL = "URL"
private const val ARG_USER_DATA = "USER_DATA"