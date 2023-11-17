package io.ssafy.mogeun.data.bluetooth

import io.ssafy.mogeun.model.BluetoothMessage

fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage? {
    val valid: Boolean = contains("#")

    if (valid) {
        val id = substringBeforeLast("#")
        val message = substringAfter("#")

        if(id.contains("#") || message.contains("#")) return null

        return BluetoothMessage(
            message = message.toInt(),
            sensorId = id.toInt(),
            isFromLocalUser = isFromLocalUser
        )
    } else {
        return null
    }
}

fun BluetoothMessage.toByteArray(): ByteArray {
    return "$sensorId#$message".encodeToByteArray()
}