package app.test.nanit.ui.screen

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import app.test.nanit.R
import app.test.nanit.model.Birthday
import app.test.nanit.model.DisplayBirthday
import app.test.nanit.ui.theme.ElephantAccent
import app.test.nanit.ui.theme.FoxAccent
import app.test.nanit.ui.theme.PelicanAccent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface BirthdayScreenContract {
    interface ViewModel {
        val state: StateFlow<State>
        val action: SharedFlow<Action>
    }

    sealed interface Action {
        data object NavigateBack : Action
    }

    data class State(
        val uiModel: UiModel = UiModel.make(Birthday.Theme.Elephant, 1),
        val name: String = "",
        val ageTimeUnit: DisplayBirthday.TimeUnit = DisplayBirthday.TimeUnit.Month,
    )

    data class UiModel(
        val backgroundImage: Int,
        @DrawableRes
        val placeholderImage: Int,
        @DrawableRes
        val ageImage: Int?,
        val backgroundColor: Color,
    ) {
        companion object Factory {
            fun make(theme: Birthday.Theme, ageNum: Int): UiModel {
                return when (theme) {
                    Birthday.Theme.Fox -> UiModel(
                        backgroundImage = R.raw.img_theme_fox,
                        placeholderImage = R.drawable.img_placeholder_fox,
                        ageImage = getAgeImage(ageNum),
                        backgroundColor = FoxAccent
                    )

                    Birthday.Theme.Elephant -> UiModel(
                        backgroundImage = R.raw.img_theme_elephant,
                        placeholderImage = R.drawable.img_placeholder_elephant,
                        ageImage = getAgeImage(ageNum),
                        backgroundColor = ElephantAccent
                    )

                    Birthday.Theme.Pelican -> UiModel(
                        backgroundImage = R.raw.img_theme_pelican,
                        placeholderImage = R.drawable.img_placeholder_pelican,
                        ageImage = getAgeImage(ageNum),
                        backgroundColor = PelicanAccent
                    )
                }
            }

            @DrawableRes
            private fun getAgeImage(ageNum: Int): Int? {
                return when (ageNum) {
                    1 -> R.drawable.img_age_1
                    2 -> R.drawable.img_age_2
                    3 -> R.drawable.img_age_3
                    4 -> R.drawable.img_age_4
                    5 -> R.drawable.img_age_5
                    6 -> R.drawable.img_age_6
                    7 -> R.drawable.img_age_7
                    8 -> R.drawable.img_age_8
                    9 -> R.drawable.img_age_9
                    10 -> R.drawable.img_age_10
                    11 -> R.drawable.img_age_11
                    else -> null
                }
            }
        }
    }
}