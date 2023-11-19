package io.ssafy.mogeun.ui.screens.setting.connection

import android.bluetooth.BluetoothDevice

data class ConnectionState(
    val scannedDevices: List<BluetoothDevice> = listOf(),
    val connectedDevices: List<BluetoothDevice?> = listOf(null, null),
    val sensorVal: List<Int> = listOf(0, 0),
)