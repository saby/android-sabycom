package ru.tensor.sabycom.data

import android.webkit.URLUtil
import androidx.annotation.Px
import java.util.regex.Pattern

/**
 * Утилита для формирования/работы со ссылками на превьювер.
 *
 * @author am.boldinov
 */
internal object PreviewerUrlUtil {

    private const val PREVIEWER_REGEX = "/previewer/"

    private const val PREVIEWER_SIZE_REGEX = "\\d+/"
    private const val PREVIEWER_SIZES_REGEX = "$PREVIEWER_SIZE_REGEX$PREVIEWER_SIZE_REGEX"
    private const val PREVIEWER_UNKNOWN_SIZES = "%d/%d"
    private const val PREVIEWER_SIZES_UNIVERSAL_REGEX =
        "($PREVIEWER_SIZES_REGEX|$PREVIEWER_UNKNOWN_SIZES/)"

    /**
     * Способ масштабирования картинки
     */
    enum class ScaleMode(private val value: String) {
        /**
         * Обрезка
         */
        CROP("c/"),

        /**
         * Масштабирование по большей стороне (изображение полностью впишется в область)
         */
        RESIZE("r/"),

        /**
         * Масштабирование по меньшей стороне (размеры изображения будут не меньше, чем заданная область)
         */
        SCALING_BY_MIN_SIDE("m/"),

        /**
         * Загрузка прогрессивного изображения
         */
        PROGRESSIVE("p/");

        /**
         * Формирование префикса превьювера с типом масштабирования и размерами
         *
         * @param prefix  префикс, содержащий [.PREVIEWER_REGEX]
         * @param width   ширина
         * @param height  высота
         * @param postfix постфикс, отображаемый после размеров
         * @return отформатированная строка
         */
        fun concatWithPrefixPostfixAndSizes(
            prefix: String,
            width: Int,
            height: Int,
            postfix: String
        ): String {
            return concatWithPrefix(prefix) + width + "/" + height + postfix
        }

        /**
         * Формирование префикса превьювера с типом масштабирования
         *
         * @param prefix префикс, содержащий [.PREVIEWER_REGEX]
         * @return отформатированная строка
         */
        fun concatWithPrefix(prefix: String): String {
            return "$prefix$value"
        }
    }

    /**
     * Формирование ссылки для изображения на previewer.
     * Если ссылка на внешний ресурс, возвращается без изменений.
     * Масштабирование по умолчанию [ScaleMode.SCALING_BY_MIN_SIDE].
     *
     * @param url       ссылка для проверки и преобразования
     * @param width     ширина
     * @param height    высота
     * @param scaleMode способ масштабирования картинки
     * @return ссылка на изображение указанных размеров, либо на внешний ресурс.
     */
    fun formatImageUrl(
        url: String?,
        @Px width: Int,
        @Px height: Int,
        scaleMode: ScaleMode = ScaleMode.SCALING_BY_MIN_SIDE
    ): String? {
        if (url.isNullOrEmpty()) {
            return null
        }
        if (URLUtil.isNetworkUrl(url)) { // ссылка сформирована полностью
            val hostUrl = UrlUtil.HOST_URL
            if (!url.contains(hostUrl)) {
                // ссылка на внешний ресурс
                return url
            }
            return if (!url.contains(PREVIEWER_REGEX)) { // если ссылка не содержит PREVIEWER_REGEX, добавляем
                Pattern.compile(hostUrl).matcher(url)
                    .replaceFirst(
                        scaleMode.concatWithPrefixPostfixAndSizes(
                            hostUrl + PREVIEWER_REGEX,
                            width,
                            height, ""
                        )
                    )
            } else { // изменяем параметры
                replacePreviewerUrlPart(url, width, height, scaleMode)
            }
        } else { // информативная часть ссылки
            return (if (url.startsWith("/")) url else "/$url").let {
                if (!it.contains(PREVIEWER_REGEX)) { // если ссылка не содержит PREVIEWER_REGEX, добавляем
                    scaleMode.concatWithPrefixPostfixAndSizes(
                        PREVIEWER_REGEX, width, height,
                        it
                    )
                } else {
                    replacePreviewerUrlPart(it, width, height, scaleMode)
                }
            }.let {
                UrlUtil.formatUrlWithHost(it)
            }
        }
    }

    /**
     * Преобразование ссылки, содержащей [.PREVIEWER_REGEX].
     * Добавление/замена параметра, отвечающего за масштабирование, установка/замена размеров.
     *
     * @param url       ссылка
     * @param width     ширина
     * @param height    высота
     * @param scaleMode способ масштабирования картинки
     * @return ссылка на изображение указанных размеров
     */
    private fun replacePreviewerUrlPart(
        url: String,
        @Px width: Int,
        @Px height: Int,
        scaleMode: ScaleMode
    ): String {
        // замену crop игнорируем, т.к. замена на resize может работать некорректно (если указано позиционирование)
        if (url.contains(ScaleMode.CROP.concatWithPrefix(PREVIEWER_REGEX))) {
            return if (scaleMode == ScaleMode.CROP) {
                resizeImagePreviewerUrl(
                    url,
                    width,
                    height,
                    ScaleMode.CROP.concatWithPrefix(PREVIEWER_REGEX),
                    ScaleMode.CROP
                )
            } else {
                url
            }
        }
        if (url.contains(ScaleMode.RESIZE.concatWithPrefix(PREVIEWER_REGEX))) {
            return resizeImagePreviewerUrl(
                url,
                width,
                height,
                ScaleMode.RESIZE.concatWithPrefix(PREVIEWER_REGEX),
                scaleMode
            )
        }
        if (url.contains(ScaleMode.SCALING_BY_MIN_SIDE.concatWithPrefix(PREVIEWER_REGEX))) {
            return resizeImagePreviewerUrl(
                url,
                width,
                height,
                ScaleMode.SCALING_BY_MIN_SIDE.concatWithPrefix(PREVIEWER_REGEX),
                scaleMode
            )
        }
        if (url.contains(ScaleMode.PROGRESSIVE.concatWithPrefix(PREVIEWER_REGEX))) {
            return resizeImagePreviewerUrl(
                url,
                width,
                height,
                ScaleMode.PROGRESSIVE.concatWithPrefix(PREVIEWER_REGEX),
                scaleMode
            )
        }
        if (Pattern.compile(PREVIEWER_REGEX + PREVIEWER_SIZES_REGEX).matcher(url)
                .find()
        ) { // ссылка на previewer без опций с размерами
            return resizeImagePreviewerUrl(
                url,
                width,
                height,
                PREVIEWER_REGEX,
                scaleMode
            )
        }
        // ссылка на previewer без опций с заглушками вместо размеров
        if (Pattern.compile(PREVIEWER_REGEX + PREVIEWER_UNKNOWN_SIZES).matcher(url).find()) {
            return resizeImagePreviewerUrl(
                url,
                width,
                height,
                PREVIEWER_REGEX,
                scaleMode
            )
        }

        val matcher = Pattern.compile(PREVIEWER_REGEX + PREVIEWER_SIZE_REGEX).matcher(url)
        return if (matcher.find()) { // ссылка на previewer без опций с размером для квадратной области
            matcher.replaceFirst(
                scaleMode.concatWithPrefixPostfixAndSizes(
                    PREVIEWER_REGEX,
                    width,
                    height,
                    "/"
                )
            )
        } else Pattern.compile(PREVIEWER_REGEX)
            .matcher(url) // ссылка на previewer без опций и размеров
            .replaceFirst(
                scaleMode.concatWithPrefixPostfixAndSizes(
                    PREVIEWER_REGEX,
                    width,
                    height,
                    "/"
                )
            )
    }

    /**
     * Преобразование ссылки, содержащей [.PREVIEWER_REGEX] и параметр, отвечающий за масштабирование.
     * Установка размеров и изменение способа масштабирования на указанный.
     *
     * @param url           ссылка
     * @param width         ширина
     * @param height        высота
     * @param replacingPart заменяемая подстрока
     * @param scaleMode     способ масштабирования картинки
     * @return ссылка на изображение указанных размеров
     */
    private fun resizeImagePreviewerUrl(
        url: String, @Px width: Int, @Px height: Int,
        replacingPart: String, scaleMode: ScaleMode
    ): String {
        val newPart = scaleMode.concatWithPrefix(PREVIEWER_REGEX)
        return insertSizeInImageUrl(
            resetImageUrlSizes(url, replacingPart, newPart), width, height
        )
    }

    /**
     * Сброс ранее установленных размеров изображения + замена параметров ссылки.
     *
     * @param imageUrl      ссылка, содержащая [.PREVIEWER_REGEX]
     * @param replacingPart заменяемая подстрока, содержащая параметры
     * @param newPart       подстрока, содержащая параметры, на которую заменяем
     * @return ссылка на изображение с пустыми размерами
     */
    private fun resetImageUrlSizes(
        imageUrl: String, replacingPart: String,
        newPart: String
    ): String {
        val pattern = Pattern.compile(replacingPart + PREVIEWER_SIZES_UNIVERSAL_REGEX)
        return pattern.matcher(imageUrl)
            .replaceFirst("$newPart$PREVIEWER_UNKNOWN_SIZES/")
    }

    /**
     * Установка новых размеров изображения.
     *
     * @param imageUrl ссылка, содержащая [.PREVIEWER_REGEX]
     * @param width    ширина
     * @param height   высота
     * @return ссылка на изображение с указанными размерами
     */
    private fun insertSizeInImageUrl(imageUrl: String, width: Int, height: Int): String {
        return imageUrl.replaceFirst(
            PREVIEWER_UNKNOWN_SIZES.toRegex(),
            "$width/$height"
        )
    }
}