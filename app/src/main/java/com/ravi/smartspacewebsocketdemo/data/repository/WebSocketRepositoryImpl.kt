package com.ravi.smartspacewebsocketdemo.data.repository

import com.ravi.smartspacewebsocketdemo.data.remote.WebSocketClient
import com.ravi.smartspacewebsocketdemo.domain.model.IoTCommand
import com.ravi.smartspacewebsocketdemo.domain.model.IoTTelemetry
import com.ravi.smartspacewebsocketdemo.domain.model.WebSocketEvent
import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WebSocketRepositoryImpl @Inject constructor(
    private val client: WebSocketClient
) : WebSocketRepository {

    override fun connect(): Flow<WebSocketEvent> = client.connect()

    override suspend fun sendCommand(command: IoTCommand) {
        client.sendCommand(command)
    }

    override suspend fun simulateTelemetry(telemetry: IoTTelemetry) {
        client.sendTelemetry(telemetry)   // sent to echo server → will come back as MessageReceived
    }

    override fun disconnect() = client.disconnect()
}
