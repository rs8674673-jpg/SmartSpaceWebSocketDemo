package com.ravi.smartspacewebsocketdemo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class IoTCommand(
    val type: String = "command",
    val deviceId: String,
    val action: String,          // "toggle", "on", "off", "get_status"
    val timestamp: Long = System.currentTimeMillis()
)
