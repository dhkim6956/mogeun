package io.ssafy.mogeun.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.SetRepository
import io.ssafy.mogeun.data.bluetooth.BluetoothController
import io.ssafy.mogeun.data.bluetooth.BluetoothDeviceDomain
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.data.bluetooth.ConnectedDeviceDomain
import io.ssafy.mogeun.data.bluetooth.Connection0Result
import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BluetoothViewModel(
    private val setRepository: SetRepository,
    private val emgRepository: EmgRepository,
    private val bluetoothController: BluetoothController
): ViewModel() {
    private val _getSetSuccess = MutableStateFlow(false)
    val getSetSuccess: StateFlow<Boolean> = _getSetSuccess.asStateFlow()
    fun getSet() {
        lateinit var ret: SetResponse
        viewModelScope.launch {
            ret = setRepository.getSet(SetRequest(1, 1, 1, 43.7F, 35, 10, 8, 12.6F, "2023-11-03T20:03:42", "2023-11-03T20:10:42"))
            Log.d("getSet", "$ret")
            if(ret.message == "SUCCESS") {
                _getSetSuccess.value = true
            }
        }
    }

    private val connectedState: Array<Boolean> = arrayOf(false, false, false, false)

    private val _state = MutableStateFlow(BluetoothUiState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        bluetoothController.connectedDevices,
        _state
    ) { scannedDevices, pairedDevices, connectedDevices, state ->

        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            connectedDevices = connectedDevices,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _state.value)

    private var deviceConnectionJob: Array<Job?> = arrayOf(null, null, null, null)



    init {
        bluetoothController.isConnected.onEach { isConnected ->
            _state.update { it.copy(isConnected = isConnected) }
        }.launchIn(viewModelScope)

        bluetoothController.errors.onEach { error ->
            _state.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain, deviceNo: Int = 0) {
        _state.update { it.copy(isConnecting = true) }

        deviceConnectionJob[deviceNo] = bluetoothController
            .connectToDevice(ConnectedDevice(name = device.name, address = device.address, assignedNo = deviceNo))
            .listen()
    }

    fun disconnectFromDevice(deviceNo: Int = 0) {
        deviceConnectionJob[deviceNo]?.cancel()
        bluetoothController.closeConnection(deviceNo)
        val bufConnected = state.value.isConnected.toMutableList()
        bufConnected[deviceNo] = false

        _state.update { it.copy(
            isConnecting = false,
            isConnected = bufConnected.toList()
        ) }
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    private fun Flow<Connection0Result>.listen(): Job {
        return onEach { result ->
            when(result) {
                is Connection0Result.ConnectionEstablished -> {
                    val bufConnected = state.value.isConnected.toMutableList()
                    bufConnected[0] = true
                    _state.update {it.copy(
                        isConnected = bufConnected.toList(),
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is Connection0Result.TransferSucceeded -> {
                    Log.d("bluetooth0", "${result.message}")


                }
                is Connection0Result.Error -> {
                    if(result.message != "errString") {
                        val bufConnected = state.value.isConnected.toMutableList()
                        bufConnected[0] = false
                        _state.update { it.copy(
                            isConnected = bufConnected.toList(),
                            isConnecting = false,
                            errorMessage = result.message
                        ) }
                    }
                }
            }
        }.catch {throwable ->
            Log.d("bluetooth", "종료조건 ${throwable}")
            bluetoothController.closeConnection(0)
            val bufConnected = state.value.isConnected.toMutableList()
            bufConnected[0] = false
            _state.update { it.copy(
                isConnected = bufConnected.toList(),
                isConnecting = false,
            ) }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        bluetoothController.release()
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}