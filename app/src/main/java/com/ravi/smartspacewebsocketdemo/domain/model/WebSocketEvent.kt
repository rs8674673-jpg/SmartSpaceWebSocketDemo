package com.ravi.smartspacewebsocketdemo.domain.model

// Sealed class for all WebSocket events (reactive & type-safe)
sealed class WebSocketEvent {
    data object Connected : WebSocketEvent()
    data class MessageReceived(val message: String) : WebSocketEvent()  // raw JSON from server
    data class Telemetry(val deviceId: String, val status: String, val value: Any) : WebSocketEvent()
    data class Error(val error: String) : WebSocketEvent()
    data object Closed : WebSocketEvent()
}
