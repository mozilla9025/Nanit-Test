package app.test.nanit.domain

import app.test.nanit.model.DisplayBirthday
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BirthdayCache @Inject constructor() {
    private val _state = MutableStateFlow<Data>(Data.Empty)
    val state: StateFlow<Data> = _state.asStateFlow()

    fun update(data: Data) {
        _state.value = data
    }

    sealed interface Data {
        data object Empty : Data
        data class Value(val birthday: DisplayBirthday) : Data
    }
}