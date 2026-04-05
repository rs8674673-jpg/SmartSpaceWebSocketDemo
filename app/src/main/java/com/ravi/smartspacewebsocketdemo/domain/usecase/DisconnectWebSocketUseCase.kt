package com.ravi.smartspacewebsocketdemo.domain.usecase

import com.ravi.smartspacewebsocketdemo.domain.repository.WebSocketRepository
import javax.inject.Inject

class DisconnectWebSocketUseCase @Inject constructor(
    private val repository: WebSocketRepository
) {
    operator fun invoke() = repository.disconnect()
}
