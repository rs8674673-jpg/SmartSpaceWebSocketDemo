@file:OptIn(ExperimentalMaterial3Api::class)

package com.ravi.smartspacewebsocketdemo.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ravi.smartspacewebsocketdemo.R
import com.ravi.smartspacewebsocketdemo.presentation.viewmodel.SmartHomeViewModel

@Composable
fun SmartHomeScreen(viewModel: SmartHomeViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Connection status
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(state.status.asString(), style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = { if (state.isConnected) viewModel.disconnect() else viewModel.connect() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isConnected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            if (state.isConnected) stringResource(R.string.disconnect)
                            else stringResource(R.string.connect_to_iot)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Fake IoT Devices
            Text(stringResource(R.string.smart_devices), style = MaterialTheme.typography.headlineSmall)
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(state.devices) { device ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(device.name.asString(), modifier = Modifier.weight(1f))
                            Switch(
                                checked = device.isOn,
                                onCheckedChange = { viewModel.toggleDevice(device.id) }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.simulateTelemetry() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.simulate_telemetry))
            }

            Spacer(Modifier.height(24.dp))

            // Live Log (real-time WebSocket messages)
            Text(stringResource(R.string.websocket_live_log), style = MaterialTheme.typography.headlineSmall)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(state.logMessages.reversed()) { log ->
                        Text(
                            text = log.asString(),
                            modifier = Modifier.padding(4.dp),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
