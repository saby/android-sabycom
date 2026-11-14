package ru.tensor.sabycom.push.manager.app

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.UiThread
import androidx.core.view.doOnLayout
import ru.tensor.sabycom.R
import ru.tensor.sabycom.push.builder.SabycomNotification

/**
 * Управляет появлением/скрытием всплывающих уведомлений.
 * Каждое уведомление представляет собой View, которая отображается поверх
 * пользовательского интерфейса, при этом не блокируя его нажатия.
 *
 * @author am.boldinov
 */
@UiThread
internal class OverlayViewController {

    private val animator = OverlayViewAnimator()

    /**
     * Показывает уведомление.
     * @param activity активность, к которой будет приаттачена View с уведомлением
     * @param notification объект уведомления
     * @param animation true если необходимо показать уведомление с анимацией, false иначе
     */
    fun show(activity: Activity, notification: SabycomNotification, animation: Boolean = true) {
        val binder = notification.inAppBinder ?: throw IllegalStateException()
        activity.tryGetRootView().postOnLayout {
            if (it.parent != null) {
                (it as ViewGroup).apply {
                    val view = findViewWithTag(notification.tag) ?: binder.create(context).apply {
                        tag = notification.tag
                        addView(this)
                        if (animation) {
                            animator.animateShow(this)
                        }
                    }
                    binder.bind(view, notification.data)
                }
            } else {
                Log.d("OverlayViewController", "View is detached, showing notification will be skipped")
            }
        }
    }

    /**
     * Скрывает уведомление.
     * @param activity активность, из иерархии которой будет удалена View с уведомлением
     * @param tag тэг уведомления, под которым оно было опубликовано
     * @param animation true если необходимо скрыть уведомление с анимацией, false иначе
     */
    fun hide(activity: Activity, tag: String, animation: Boolean = true) {
        activity.getRootView()?.apply {
            findViewWithTag<View>(tag)?.let { target ->
                val action = {
                    removeView(target)
                    if (childCount == 0) {
                        removeSelf()
                    }
                }
                if (animation) {
                    animator.animateHide(target) {
                        onFinish {
                            action()
                        }
                    }
                } else {
                    action()
                }
            }
        }
    }

    /**
     * Скрывает все уведомления.
     * @param activity активность, из иерархии которой будут удалены все View с уведомлениями
     */
    fun hideAll(activity: Activity) {
        activity.getRootView()?.apply {
            removeAllViews()
            removeSelf()
        }
    }

    private fun Activity.tryGetRootView(): ViewGroup {
        return getRootView() ?: run {
            val view = FrameLayout(this).apply {
                id = getRootViewId()
            }
            addContentView(
                view, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM
                )
            )
            return view
        }
    }

    private fun Activity.getRootView(): ViewGroup? {
        return findViewById(getRootViewId())
    }

    private fun View.removeSelf() {
        (parent as? ViewGroup)?.removeView(this)
    }

    private fun getRootViewId(): Int {
        return R.id.sabycom_notification_overlay_root_view
    }

    private inline fun View.postOnLayout(crossinline action: (view: View) -> Unit) {
        doOnLayout {
            if (it.isInLayout) {
                it.post {
                    action(it)
                }
            } else {
                action(it)
            }
        }
    }
}