package app.test.nanit.ui.screen

import app.test.nanit.model.Birthday
import app.test.nanit.ws.ConnectionState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MainScreenContract {

    interface ViewModel {
        val state: StateFlow<State>
        val action: SharedFlow<Action>

        fun onEvent(event: Event)
    }

    data class State(
        val ipAddress: String = "",
        val connectionState: ConnectionState = ConnectionState.Disconnected,
    )

    sealed interface Action {
        data class NavigateToBirthday(
            val birthday: Birthday
        ) : Action
    }

    sealed interface Event {
        class OnIpValueTyped(val ip: String): Event
        data object Connect : Event
        data object Disconnect : Event
        data object SendMessage : Event
    }
}