package io.ssafy.mogeun.data.bluetooth

import io.ssafy.mogeun.model.BluetoothMessage

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage {
    val id = substringBeforeLast("#")
    val message = substringAfter("#")

    return BluetoothMessage(
        message = message.toInt(),
        sensorId = id.toInt(),
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$sensorId#$message".encodeToByteArray()
}