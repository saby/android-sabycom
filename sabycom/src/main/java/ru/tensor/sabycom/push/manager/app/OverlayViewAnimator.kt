package ru.tensor.sabycom.push.manager.app

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator
import androidx.annotation.UiThread

/**
 * Аниматор для работы со всплывающими View поверх пользовательского интерфейса.
 *
 * @author am.boldinov
 */
@UiThread
internal class OverlayViewAnimator {

    /**
     * Анимирует появление View.
     */
    fun animateShow(target: View, listener: (AnimatorListener.() -> Unit)? = null) {
        target.apply {
            scaleX = 0.7f
            scaleY = 0.7f
            alpha = 0.7f
            animate().scaleX(1.0f)
                .scaleY(1.0f)
                .alpha(1.0f)
                .setDuration(300)
                .setInterpolator(OvershootInterpolator())
                .setListener(listener)
                .start()
        }
    }

    /**
     * Анимирует скрытие View.
     */
    fun animateHide(target: View, listener: (AnimatorListener.() -> Unit)? = null) {
        target.apply {
            animate().scaleX(0.5f)
                .scaleY(0.5f)
                .alpha(0.0f)
                .setDuration(100)
                .setInterpolator(LinearInterpolator())
                .setListener(listener)
                .start()
        }
    }

    private fun ViewPropertyAnimator.setListener(
        block: (AnimatorListener.() -> Unit)?
    ): ViewPropertyAnimator {
        block?.let {
            setListener(AnimatorListener().apply(it))
        }
        return this
    }

    internal class AnimatorListener : AnimatorListenerAdapter() {

        private var onStart: ((Animator) -> Unit)? = null
        private var onFinish: ((Animator) -> Unit)? = null

        fun onStart(listener: (Animator) -> Unit) {
            onStart = listener
        }

        fun onFinish(listener: (Animator) -> Unit) {
            onFinish = listener
        }

        override fun onAnimationStart(animation: Animator) {
            onStart?.invoke(animation)
        }

        override fun onAnimationEnd(animation: Animator) {
            onFinish?.invoke(animation)
        }
    }
}