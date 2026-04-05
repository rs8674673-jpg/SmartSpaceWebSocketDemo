package com.ravi.smartspacewebsocketdemo.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class IoTTelemetry(
    val type: String = "telemetry",
    val deviceId: String,
    val status: String,     // "on", "off", "online"
    val value: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
