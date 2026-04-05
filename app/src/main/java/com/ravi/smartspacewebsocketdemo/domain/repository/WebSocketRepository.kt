package com.ravi.smartspacewebsocketdemo.domain.repository

import com.ravi.smartspacewebsocketdemo.domain.model.IoTCommand
import com.ravi.smartspacewebsocketdemo.domain.model.IoTTelemetry
import com.ravi.smartspacewebsocketdemo.domain.model.WebSocketEvent
import kotlinx.coroutines.flow.Flow

interface WebSocketRepository {
    fun connect(): Flow<WebSocketEvent>
    suspend fun sendCommand(command: IoTCommand)
    suspend fun simulateTelemetry(telemetry: IoTTelemetry)
    fun disconnect()
}
