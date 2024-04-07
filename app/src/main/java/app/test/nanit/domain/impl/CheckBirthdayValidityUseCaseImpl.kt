package app.test.nanit.domain.impl

import android.util.Log
import app.test.nanit.domain.CheckBirthdayValidityUseCase
import app.test.nanit.model.Birthday
import app.test.nanit.model.DisplayBirthday
import com.squareup.moshi.Moshi
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.monthsUntil
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import javax.inject.Inject

class CheckBirthdayValidityUseCaseImpl @Inject constructor(
    private val moshi: Moshi
) : CheckBirthdayValidityUseCase {
    override fun invoke(jsonBirthday: String): DisplayBirthday? {
        val birthday = moshi.adapter(Birthday::class.java).fromJson(jsonBirthday) ?: return null

        val timeZone = TimeZone.currentSystemDefault()
        val dobInstant = Instant.fromEpochMilliseconds(birthday.dob)
        val dob = dobInstant.toLocalDateTime(timeZone)
        val currentDateInstant = Clock.System.now()
        val currentDate = currentDateInstant.toLocalDateTime(timeZone)

        if (currentDate.dayOfMonth != dob.dayOfMonth) {
            Log.d(
                "CheckBirthdayValidityUseCase",
                "Days are not matched. Current: $currentDate, received: $dob"
            )
            return null
        }

        val months = dobInstant.monthsUntil(currentDateInstant, timeZone)

        val (age, timeUnit) = when {
            months < 12 -> months to DisplayBirthday.TimeUnit.Month
            else -> if (currentDate.month != dob.month) {
                null to null
            } else {
                months / 12 to DisplayBirthday.TimeUnit.Year
            }
        }

        if (age == null || timeUnit == null) return null

        return DisplayBirthday(
            id = UUID.randomUUID().toString(),
            theme = birthday.theme,
            age = age,
            ageTimeUnit = timeUnit,
            name = birthday.name
        )
    }
}