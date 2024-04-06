package app.test.nanit.ws

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface WsClient {
    val connectionState: StateFlow<ConnectionState>
    val messages: Flow<String>

    fun connect(socketUrl: String)
    fun emit(message: String)
    fun disconnect()
}