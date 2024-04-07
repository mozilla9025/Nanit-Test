package app.test.nanit.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DisplayBirthday(
    val id: String,
    val theme: Birthday.Theme,
    val age: Int,
    val ageTimeUnit: TimeUnit,
    val name: String,
) : Parcelable {
    enum class TimeUnit {
        Month, Year
    }
}