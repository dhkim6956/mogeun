package io.ssafy.mogeun.ui.screens.setting.connection

import io.ssafy.mogeun.data.bluetooth.BluetoothDevice
import io.ssafy.mogeun.model.BluetoothMessage

data class ConnectionUiState (
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val connectedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)