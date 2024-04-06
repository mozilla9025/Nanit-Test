package app.test.nanit.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.test.nanit.model.Birthday
import app.test.nanit.util.mutate
import app.test.nanit.ws.ConnectionState
import app.test.nanit.ws.WsClient
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wsClient: WsClient,
    private val moshi: Moshi
) : ViewModel(), MainScreenContract.ViewModel {

    private val _state = MutableStateFlow(MainScreenContract.State(ConnectionState.Disconnected))
    private val _action = MutableSharedFlow<MainScreenContract.Action>()

    override val state: StateFlow<MainScreenContract.State> = _state.asStateFlow()
    override val action: SharedFlow<MainScreenContract.Action> = _action.asSharedFlow()

    init {
        observeConnectionState()
        observeMessages()
    }

    override fun onEvent(event: MainScreenContract.Event) {
        when (event) {
            is MainScreenContract.Event.Connect -> onConnect(event.ip)
            MainScreenContract.Event.Disconnect -> onDisconnect()
            MainScreenContract.Event.SendMessage -> onSendMessage()
        }
    }

    private fun onConnect(ip: String) {
        wsClient.connect("ws://${ip}/nanit")
    }

    private fun onDisconnect() {
        wsClient.disconnect()
    }

    private fun onSendMessage() {
        wsClient.emit("HappyBirthday")
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            wsClient.connectionState.collect { _state.mutate { copy(connectionState = it) } }
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            wsClient.messages
                .map { moshi.adapter(Birthday::class.java).fromJson(it) }
                .filterNotNull()
                .collect {
                    _action.emit(MainScreenContract.Action.NavigateToBirthday(it))
                }
        }
    }
}