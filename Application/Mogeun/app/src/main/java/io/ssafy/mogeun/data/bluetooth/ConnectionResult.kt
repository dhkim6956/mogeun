package io.ssafy.mogeun.data.bluetooth

import io.ssafy.mogeun.model.BluetoothMessage

sealed interface ConnectionResult {
    object ConnectionEstablished: ConnectionResult
    data class TransferSucceeded(val message: BluetoothMessage): ConnectionResult
    data class Error(val message: String): ConnectionResult
}
