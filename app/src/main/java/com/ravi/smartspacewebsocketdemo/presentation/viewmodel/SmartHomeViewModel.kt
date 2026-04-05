package com.ravi.smartspacewebsocketdemo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.smartspacewebsocketdemo.R
import com.ravi.smartspacewebsocketdemo.domain.model.IoTCommand
import com.ravi.smartspacewebsocketdemo.domain.model.IoTTelemetry
import com.ravi.smartspacewebsocketdemo.domain.model.WebSocketEvent
import com.ravi.smartspacewebsocketdemo.domain.usecase.ConnectWebSocketUseCase
import com.ravi.smartspacewebsocketdemo.domain.usecase.DisconnectWebSocketUseCase
import com.ravi.smartspacewebsocketdemo.domain.usecase.SendCommandUseCase
import com.ravi.smartspacewebsocketdemo.domain.usecase.SimulateTelemetryUseCase
import com.ravi.smartspacewebsocketdemo.presentation.ui.state.Device
import com.ravi.smartspacewebsocketdemo.presentation.ui.state.SmartHomeUiState
import com.ravi.smartspacewebsocketdemo.presentation.ui.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SmartHomeViewModel @Inject constructor(
    private val connectUseCase: ConnectWebSocketUseCase,
    private val sendCommandUseCase: SendCommandUseCase,
    private val simulateTelemetryUseCase: SimulateTelemetryUseCase,
    private val disconnectUseCase: DisconnectWebSocketUseCase
) : ViewModel() {

    private val TAG = "SmartHomeViewModel"

    private val _uiState = MutableStateFlow(SmartHomeUiState())
    val uiState: StateFlow<SmartHomeUiState> = _uiState.asStateFlow()

    private var isConnected = false

    // Fake IoT devices (in-memory state)
    private val devices = mutableMapOf(
        "light1" to Device("light1", UiText.StringResource(R.string.device_living_room_light), "light", false),
        "fan1" to Device("fan1", UiText.StringResource(R.string.device_bedroom_fan), "fan", false)
    )

    fun connect() {
        Log.d(TAG, "Connecting to WebSocket...")
        connectUseCase().onEach { event ->
            Log.d(TAG, "WebSocket Event: $event")
            when (event) {
                is WebSocketEvent.Connected -> {
                    isConnected = true
                    _uiState.value = _uiState.value.copy(
                        isConnected = true,
                        status = UiText.StringResource(R.string.status_connected)
                    )
                }
                is WebSocketEvent.MessageReceived -> handleIncomingMessage(event.message)
                is WebSocketEvent.Error -> {
                    _uiState.value = _uiState.value.copy(
                        status = UiText.StringResource(R.string.status_error, event.error)
                    )
                }
                is WebSocketEvent.Closed -> {
                    isConnected = false
                    _uiState.value = _uiState.value.copy(
                        isConnected = false,
                        status = UiText.StringResource(R.string.status_disconnected)
                    )
                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    private fun handleIncomingMessage(json: String) {
        Log.d(TAG, "Incoming Message: $json")
        // In real app you would parse properly. Here we simulate both command ack and telemetry
        val newDevices = devices.toMutableMap()
        // Simple string check for demo (you can add full deserialization)
        if (json.contains("telemetry") || json.contains("light1") || json.contains("fan1")) {
            // Update UI state
            _uiState.value = _uiState.value.copy(
                devices = newDevices.values.toList(),
                logMessages = _uiState.value.logMessages + UiText.StringResource(R.string.log_message_received, json)
            )
        }
    }

    fun toggleDevice(deviceId: String) {
        Log.d(TAG, "Toggling device: $deviceId")
        viewModelScope.launch {
            val device = devices[deviceId] ?: return@launch
            val newStatus = !device.isOn
            device.isOn = newStatus

            val command = IoTCommand(deviceId = deviceId, action = if (newStatus) "on" else "off")
            Log.d(TAG, "Sending command: $command")
            sendCommandUseCase(command)

            // Update local UI immediately (optimistic update)
            _uiState.value = _uiState.value.copy(
                devices = devices.values.toList(),
                logMessages = _uiState.value.logMessages + UiText.StringResource(R.string.log_sent_command, command.toString())
            )
        }
    }

    fun simulateTelemetry() {
        Log.d(TAG, "Simulating telemetry...")
        viewModelScope.launch {
            // Simulate a real IoT device pushing status (e.g. motion detected, temp change)
            val fakeTelemetry = IoTTelemetry(
                deviceId = "light1",
                status = if (Math.random() > 0.5) "on" else "off",
                value = "auto"
            )
            Log.d(TAG, "Sending fake telemetry: $fakeTelemetry")
            simulateTelemetryUseCase(fakeTelemetry)

            _uiState.value = _uiState.value.copy(
                logMessages = _uiState.value.logMessages + UiText.StringResource(R.string.log_telemetry_simulated)
            )
        }
    }

    fun disconnect() {
        Log.d(TAG, "Disconnecting WebSocket...")
        disconnectUseCase()
        isConnected = false
    }
}
