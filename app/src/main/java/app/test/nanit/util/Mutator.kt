package app.test.nanit.util

import kotlinx.coroutines.flow.MutableStateFlow

suspend fun <T> MutableStateFlow<T>.mutate(mutator: T.() -> T) {
    val value = this.value
    this.emit(value.mutator())
}