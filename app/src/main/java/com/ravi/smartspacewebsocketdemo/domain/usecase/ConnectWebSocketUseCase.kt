package com.ravi.smartspacewebsocketdemo.domain.usecase

import com.ravi.smartspacewebsocketdemo.domain.model.WebSocketEvent
import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectWebSocketUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke(): Flow<WebSocketEvent> = repository.connect()
}
