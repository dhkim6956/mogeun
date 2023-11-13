package io.ssafy.mogeun.ui

import io.ssafy.mogeun.data.bluetooth.BluetoothDevice
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.model.BluetoothMessage

data class BluetoothUiState (
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val connectedDevices: List<ConnectedDevice> = emptyList(),
    val isConnected: List<Boolean> = listOf(false, false, false, false),
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val subscribe: Boolean = false,
)