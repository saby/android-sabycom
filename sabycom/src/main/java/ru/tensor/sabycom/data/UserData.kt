package ru.tensor.sabycom.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

/**
 * Информация о пользователе. Если ни какой информации еще не известно все равно необходимо передавать идентификатор
 * пользователя. Прикладной разработчик должен позаботиться о том что бы между сессиями идентификатор был одинаковый
 *
 * @author ma.kolpakov
 */
@Parcelize
data class UserData(
    val id: UUID,
    val name: String? = null,
    val surname: String? = null,
    val email: String? = null,
    val phone: String? = null,
) : Parcelable
