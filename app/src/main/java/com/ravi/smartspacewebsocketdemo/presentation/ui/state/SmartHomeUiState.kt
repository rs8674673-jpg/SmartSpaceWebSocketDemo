package com.ravi.smartspacewebsocketdemo.presentation.ui.state

import com.ravi.smartspacewebsocketdemo.R
import com.ravi.smartspacewebsocketdemo.presentation.ui.util.UiText

data class Device(
    val id: String,
    val name: UiText,
    val type: String,
    var isOn: Boolean
)

data class SmartHomeUiState(
    val isConnected: Boolean = false,
    val status: UiText = UiText.StringResource(R.string.status_initial_disconnected),
    val devices: List<Device> = emptyList(),
    val logMessages: List<UiText> = emptyList()
)
