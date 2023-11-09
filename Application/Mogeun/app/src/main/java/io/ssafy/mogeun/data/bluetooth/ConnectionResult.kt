package io.ssafy.mogeun.data.bluetooth

import io.ssafy.mogeun.model.BluetoothMessage

sealed interface Connection1Result {
    object ConnectionEstablished: Connection1Result
    data class TransferSucceeded(val message: BluetoothMessage): Connection1Result
    data class Error(val message: String): Connection1Result
}