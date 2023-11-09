package io.ssafy.mogeun.data.bluetooth

typealias ConnectedDeviceDomain = ConnectedDevice

data class ConnectedDevice(
    val name: String?,
    val address: String,
    val assignedNo: Int
)

