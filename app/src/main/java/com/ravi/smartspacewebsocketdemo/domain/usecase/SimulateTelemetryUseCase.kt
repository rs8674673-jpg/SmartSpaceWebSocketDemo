package com.ravi.smartspacewebsocketdemo.domain.usecase

import com.ravi.smartspacewebsocketdemo.domain.model.IoTTelemetry
import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import javax.inject.Inject

class SimulateTelemetryUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(telemetry: IoTTelemetry) = repository.simulateTelemetry(telemetry)
}
