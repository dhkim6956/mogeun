package io.ssafy.mogeun.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.SetRepository
import io.ssafy.mogeun.data.bluetooth.BluetoothController
import io.ssafy.mogeun.data.bluetooth.BluetoothDeviceDomain
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.data.bluetooth.Connection1Result
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
            messages = if(state.isConnected) state.messages else emptyList()
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


        state.value.connectedDevices.forEach {  }

        deviceConnectionJob[deviceNo] = bluetoothController
            .connectToDevice(ConnectedDevice(name = device.name, address = device.address, assignedNo = deviceNo))
            .listen()
    }

    fun disconnectFromDevice(deviceNo: Int = 0) {
        deviceConnectionJob[deviceNo]?.cancel()
        bluetoothController.closeConnection(deviceNo)
        _state.update { it.copy(
            isConnecting = false,
            isConnected = false
        ) }
    }

//    fun waitForIncomingConnections() {
//        _state.update { it.copy(isConnected = true) }
//        deviceConnectionJob = bluetoothController
//            .startBluetoothServer()
//            .listen()
//    }
//
//    fun sendMessage(message: Int) {
//        viewModelScope.launch {
//            val bluetoothMessage = bluetoothController.trySendMessage(message)
//            if(bluetoothMessage != null) {
//                _state.update { it.copy(
//                    messages = it.messages + bluetoothMessage
//                ) }
//            }
//        }
//    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    private fun Flow<Connection1Result>.listen(): Job {
        return onEach { result ->
            when(result) {
                is Connection1Result.ConnectionEstablished -> {
                    _state.update {it.copy(
                        isConnected = true,
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is Connection1Result.TransferSucceeded -> {
                    Log.d("bluetooth", "${result.message}")
                    _state.update { it.copy(
                        messages = it.messages + result.message
                    ) }
                }
                is Connection1Result.Error -> {
                    if(result.message != "errString") {
                        _state.update { it.copy(
                            isConnected = false,
                            isConnecting = false,
                            errorMessage = result.message
                        ) }
                    }
                }
            }
        }.catch {throwable ->
            Log.d("bluetooth", "종료조건 ${throwable}")
            bluetoothController.closeConnection(1)
            _state.update { it.copy(
                isConnected = false,
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