package app.test.nanit.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Birthday(
    @Json(name = "name")
    val name: String,
    @Json(name = "dob")
    val dob: Long,
    @Json(name = "theme")
    val theme: Theme
) {
    enum class Theme {
        @Json(name = "fox")
        Fox,

        @Json(name = "elephant")
        Elephant,

        @Json(name = "pelican")
        Pelican
    }
}