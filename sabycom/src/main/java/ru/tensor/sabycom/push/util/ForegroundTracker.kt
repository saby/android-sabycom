package ru.tensor.sabycom.push.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

/**
 * Трекер для подписки на события перехода приложения на передний или на задний план.
 *
 * @see registerForegroundTracker
 * @see unregisterForegroundTracker
 * @see Application.registerActivityLifecycleCallbacks
 *
 * @author am.boldinov
 */
internal class ForegroundTracker(
    init: ForegroundTracker.() -> Unit
) : Application.ActivityLifecycleCallbacks {

    private var foreground: Activity? = null

    private var onForeground: ((Activity) -> Unit)? = null
    private var onBackground: ((Activity) -> Unit)? = null

    init {
        init()
    }

    fun onForeground(listener: (Activity) -> Unit) {
        onForeground = listener
    }

    fun onBackground(listener: (Activity) -> Unit) {
        onBackground = listener
    }

    fun launchOnForeground(block: (Activity) -> Unit): Boolean {
        return foreground?.let {
            block.invoke(it)
            true
        } ?: false
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {
        foreground = activity
        onForeground?.invoke(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        if (foreground == activity) {
            foreground = null
        }
        onBackground?.invoke(activity)
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }
}

internal fun Context.registerForegroundTracker(
    init: ForegroundTracker.() -> Unit
): ForegroundTracker {
    return ForegroundTracker(init).apply {
        (applicationContext as Application).registerActivityLifecycleCallbacks(this)
    }
}

internal fun Context.unregisterForegroundTracker(tracker: ForegroundTracker) {
    (applicationContext as Application).unregisterActivityLifecycleCallbacks(tracker)
}