package io.ssafy.mogeun.model

data class BluetoothMessage (
    val message: Int,
    val sensorId: Int,
    val isFromLocalUser: Boolean
)