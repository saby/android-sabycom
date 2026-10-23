package ru.tensor.sabycom.push.builder.chat

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequestBuilder
import ru.tensor.sabycom.R
import ru.tensor.sabycom.data.PreviewerUrlUtil

private const val CLOSE_OVERLAP_WIDTH_FACTOR = 0.4
private const val CLOSE_OVERLAP_HEIGHT_FACTOR = 0.2

/**
 * Layout для отображения всплывающего уведомления по сообщениям из чата
 * виджета СБИС онлайн консультант.
 *
 * @author am.boldinov
 */
internal class ChatNotificationLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val contentLayout: View
    private val avatarView: SimpleDraweeView
    private val titleView: TextView
    private val subtitleView: TextView
    private val dateView: TextView
    private val counterView: TextView
    private val closeView: ImageView

    private val closeOverlapWidth: Int
    private val closeOverlapHeight: Int

    init {
        contentLayout = LayoutInflater.from(context)
            .inflate(R.layout.sabycom_notification_chat_content_layout, this, false).apply {
                (layoutParams as? LayoutParams)?.gravity =
                    Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
                clipToOutline = true
                clipToPadding = false
                background = getContentBackgroundDrawable()
                avatarView = findViewById(R.id.sabycom_notification_chat_avatar)
                titleView = findViewById(R.id.sabycom_notification_chat_title)
                subtitleView = findViewById(R.id.sabycom_notification_chat_subtitle)
                dateView = findViewById(R.id.sabycom_notification_chat_date)
                counterView = findViewById(R.id.sabycom_notification_chat_counter)
            }
        closeView = AppCompatImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            translationZ = contentLayout.elevation + 1
            setImageResource(R.drawable.notification_close_drawable)
            closeOverlapWidth = (drawable.intrinsicWidth * CLOSE_OVERLAP_WIDTH_FACTOR).toInt()
            closeOverlapHeight = (drawable.intrinsicHeight * CLOSE_OVERLAP_HEIGHT_FACTOR).toInt()
        }
        setPadding(resources.getDimensionPixelSize(R.dimen.sabycom_notification_chat_content_padding))
        addView(contentLayout)
        addView(closeView)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            overlapMeasureSpec(widthMeasureSpec, closeOverlapWidth),
            overlapMeasureSpec(heightMeasureSpec, closeOverlapHeight)
        )
        setMeasuredDimension(measuredWidth + closeOverlapWidth, measuredHeight + closeOverlapHeight)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        val closeRight = contentLayout.right + closeOverlapWidth
        val closeTop = contentLayout.top - closeOverlapHeight
        closeView.layout(
            closeRight - closeView.measuredWidth,
            closeTop,
            closeRight,
            closeTop + closeView.measuredHeight
        )
    }

    /**
     * Устанавливает слушатель нажатий на контейнер с содержимым уведомления.
     */
    override fun setOnClickListener(l: OnClickListener?) {
        contentLayout.setOnClickListener(l)
    }

    /**
     * Устанавливает слушатель нажатий на кнопку закрытия уведомления.
     */
    fun setOnCloseClickListener(l: OnClickListener?) {
        closeView.setOnClickListener(l)
    }

    /**
     * Устанавливает ссылку для отображения фото оператора.
     */
    fun setAvatarUrl(url: String?) {
        url.takeUnless {
            it.isNullOrEmpty()
        }?.let {
            val controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(
                    ImageRequestBuilder.newBuilderWithSource(formatAvatarUri(it)).build()
                )
                .setOldController(avatarView.controller)
                .setRetainImageOnFailure(true)
                .build()
            avatarView.controller = controller
            avatarView.isVisible = true
        } ?: run {
            avatarView.setImageURI(null as Uri?, null)
            avatarView.isVisible = false
        }
    }

    /**
     * Устанавливает заголовок сообщения.
     */
    fun setTitle(title: CharSequence) {
        titleView.text = title
    }

    /**
     * Устанавливает подзаголовок (текст) сообщения.
     */
    fun setSubtitle(subtitle: CharSequence) {
        subtitleView.text = subtitle
    }

    /**
     * Устанавливает отформатированную дату отправки сообщения.
     */
    fun setDate(date: CharSequence) {
        dateView.text = date
    }

    /**
     * Устанавливает счетчик непрочитанных сообщений.
     * Отображается только при положительных значениях.
     */
    fun setCounter(counter: Int) {
        counterView.apply {
            if (counter > 0) {
                text = counter.toString()
                isVisible = true
            } else {
                isVisible = false
            }
        }
    }

    private fun overlapMeasureSpec(measureSpec: Int, overlap: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        return if (mode != MeasureSpec.UNSPECIFIED) {
            MeasureSpec.makeMeasureSpec(
                MeasureSpec.getSize(measureSpec) - overlap,
                mode
            )
        } else {
            measureSpec
        }
    }

    private fun getContentBackgroundDrawable(): Drawable {
        val drawables = arrayOf(GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.WHITE)
            cornerRadius = resources.getDimension(R.dimen.sabycom_notification_chat_corner_radius)
        }, with(TypedValue()) {
            context.theme.resolveAttribute(
                R.attr.selectableItemBackground, this, true
            )
            ContextCompat.getDrawable(context, resourceId)
        })
        return LayerDrawable(drawables)
    }

    private fun formatAvatarUri(url: String): Uri {
        return Uri.parse(
            PreviewerUrlUtil.formatImageUrl(
                url,
                avatarView.layoutParams.width,
                avatarView.layoutParams.height
            ) ?: url
        )
    }
}