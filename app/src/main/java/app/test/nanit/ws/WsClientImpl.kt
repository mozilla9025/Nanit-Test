package app.test.nanit.ws

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class WsClientImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketListener(), WsClient {

    private val _connectionState = MutableStateFlow(ConnectionState.Disconnected)
    private val _messages = MutableSharedFlow<String>(replay = 1)
    private var webSocket: WebSocket? = null

    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()
    override val messages: Flow<String> = _messages.asSharedFlow()

    override fun connect(socketUrl: String) {
        disconnect()

        Log.d("WS", "Connect: $socketUrl")

        val request = Request.Builder()
            .url(socketUrl)
            .build()
        webSocket = okHttpClient.newWebSocket(request = request, listener = this)
    }

    override fun emit(message: String) {
        Log.d("WS", "Sending: $message")

        webSocket?.send(message)
    }

    override fun disconnect() {
        Log.d("WS", "Disconnect...")
        webSocket?.close(1000, "Normal disconnect")
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WS", "Opened: $response")
        _connectionState.value = ConnectionState.Connected
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WS", "Closing: $code, $reason")

        _connectionState.value = ConnectionState.Disconnecting
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WS", "Closed: $code, $reason")

        _connectionState.value = ConnectionState.Disconnected
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WS", "On message: $text")

        _messages.tryEmit(text)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.e("WS", "On fail: $t $response")
    }
}