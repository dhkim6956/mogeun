package io.ssafy.mogeun.ui.screens.menu.connection

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.BleRepository
import io.ssafy.mogeun.model.BleDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ConnectionViewModel(
    private val bleRepository: BleRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConnectionState())
    val state: StateFlow<ConnectionState> = combine(
        bleRepository.scanResults,
        bleRepository.sensorVal,
        bleRepository.connectedDevices,
        _state
    ) { scanResult, sensorVal, connectedDevices, state ->
        state.copy(
            scannedDevices = scanResult,
            connectedDevices = connectedDevices,
            sensorVal = sensorVal,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _state.value)

    fun startScan() {
        bleRepository.startScan()
    }

    fun connect(device: BluetoothDevice) {
        bleRepository.connect(device)
    }

    fun disConnect(device: BleDevice) {
        if(bleRepository.virtualEnabled.value) return
        bleRepository.disconnect(device)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}