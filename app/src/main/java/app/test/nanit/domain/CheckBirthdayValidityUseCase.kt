package app.test.nanit.domain

import app.test.nanit.model.DisplayBirthday

interface CheckBirthdayValidityUseCase {
    operator fun invoke(jsonBirthday: String): DisplayBirthday?
}