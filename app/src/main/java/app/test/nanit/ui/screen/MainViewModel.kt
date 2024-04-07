package app.test.nanit.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.test.nanit.domain.BirthdayCache
import app.test.nanit.domain.CheckBirthdayValidityUseCase
import app.test.nanit.util.mutate
import app.test.nanit.ws.WsClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wsClient: WsClient,
    private val checkBirthdayValidity: CheckBirthdayValidityUseCase,
    private val birthdayCache: BirthdayCache
) : ViewModel(), MainScreenContract.ViewModel {

    private val _state = MutableStateFlow(MainScreenContract.State())
    private val _action = MutableSharedFlow<MainScreenContract.Action>()

    override val state: StateFlow<MainScreenContract.State> = _state.asStateFlow()
    override val action: SharedFlow<MainScreenContract.Action> = _action.asSharedFlow()

    init {
        observeConnectionState()
        observeMessages()
    }

    override fun onEvent(event: MainScreenContract.Event) {
        when (event) {
            is MainScreenContract.Event.OnIpValueTyped -> onIpValueTyped(event.ip)
            MainScreenContract.Event.Connect -> onConnect()
            MainScreenContract.Event.Disconnect -> onDisconnect()
            MainScreenContract.Event.SendMessage -> onSendMessage()
        }
    }

    private fun onIpValueTyped(ip: String) {
        viewModelScope.launch {
            _state.mutate { copy(ipAddress = ip) }
        }
    }

    private fun onConnect() {
        val typedIp = _state.value.ipAddress
        wsClient.connect("ws://${typedIp}/nanit")
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
                .map { checkBirthdayValidity(it) }
                .filterNotNull()
                .onEach { birthdayCache.update(BirthdayCache.Data.Value(it)) }
                .collect {
                    _action.emit(MainScreenContract.Action.NavigateToBirthday(it))
                }
        }
    }
}