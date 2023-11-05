package io.ssafy.mogeun.ui.screens.setting.connection

import io.ssafy.mogeun.data.bluetooth.BluetoothDevice

data class ConnectionUiState (
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
)