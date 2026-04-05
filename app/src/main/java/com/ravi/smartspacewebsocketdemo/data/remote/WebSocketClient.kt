package com.ravi.smartspacewebsocketdemo.data.remote

import com.ravi.smartspacewebsocketdemo.domain.model.IoTCommand
import com.ravi.smartspacewebsocketdemo.domain.model.IoTTelemetry
import com.ravi.smartspacewebsocketdemo.domain.model.WebSocketEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebSocketClient @Inject constructor() {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(0, TimeUnit.MILLISECONDS)  // keep alive
        .build()

    //private val WS_URL = "wss://echo.websocket.events"   // FAKE IoT backend (echo server)
    private val WS_URL = "wss://ws.postman-echo.com/raw"  // FAKE IoT backend (echo server)

    fun connect(): Flow<WebSocketEvent> = callbackFlow {
        val request = Request.Builder().url(WS_URL).build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                trySend(WebSocketEvent.Connected)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(WebSocketEvent.MessageReceived(text))
                // In real IoT, parse and convert to Telemetry
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // Binary not used in this demo
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                trySend(WebSocketEvent.Closed)
                close()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                trySend(WebSocketEvent.Error(t.message ?: "Unknown error"))
            }
        })

        awaitClose {
            webSocket?.close(1000, "Client closed")
        }
    }

    fun sendCommand(command: IoTCommand) {
        val json = Json.encodeToString(IoTCommand.serializer(), command)
        webSocket?.send(json)
    }

    fun sendTelemetry(telemetry: IoTTelemetry) {
        val json = Json.encodeToString(IoTTelemetry.serializer(), telemetry)
        webSocket?.send(json)
    }

    fun disconnect() {
        webSocket?.close(1000, "Normal closure")
        webSocket = null
    }
}
