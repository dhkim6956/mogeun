package io.ssafy.mogeun.ui.screens.setting.connection

import io.ssafy.mogeun.data.bluetooth.BluetoothDevice

data class ConnectionUiState (
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList()
)