package com.ravi.smartspacewebsocketdemo.domain.usecase

import com.ravi.smartspacewebsocketdemo.domain.model.IoTCommand
import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import javax.inject.Inject

class SendCommandUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    suspend operator fun invoke(command: IoTCommand) = repository.sendCommand(command)
}
