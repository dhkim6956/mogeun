package io.ssafy.mogeun.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.data.SetRepository
import io.ssafy.mogeun.data.bluetooth.BluetoothController
import io.ssafy.mogeun.data.bluetooth.BluetoothDeviceDomain
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.data.bluetooth.ConnectedDeviceDomain
import io.ssafy.mogeun.data.bluetooth.Connection0Result
import io.ssafy.mogeun.data.bluetooth.Connection1Result
import io.ssafy.mogeun.model.BluetoothMessage
import io.ssafy.mogeun.model.SetRequest
import io.ssafy.mogeun.model.SetResponse
import io.ssafy.mogeun.ui.screens.routine.execution.EmgUiState
import io.ssafy.mogeun.ui.screens.routine.execution.RoutineState
import io.ssafy.mogeun.ui.screens.sample.DbSampleViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class BluetoothViewModel(
    private val setRepository: SetRepository,
    private val emgRepository: EmgRepository,
    private val routineRepository: RoutineRepository,
    private val bluetoothController: BluetoothController
): ViewModel() {


    private val _routineState = MutableStateFlow(RoutineState(null, false))
    val routineState = _routineState.asStateFlow()

    fun getPlanList(routineKey: Int) {
        Log.d("execution", "api called")
        viewModelScope.launch {
            val ret = routineRepository.listMyExercise(routineKey)
            _routineState.update { routineState -> routineState.copy(planList = ret) }
            Log.d("execution", "${ret}")
        }
    }

    private val _emgState = MutableStateFlow(EmgUiState())
    val emgState: StateFlow<EmgUiState> =
        emgRepository.getEmgStream()
            .map {
                if(it != null) {
                    if(it.deviceId == 0) {
                        _emgState.update { emgUiState -> emgUiState.copy(emg1 = it) }
                    } else {
                        _emgState.update { emgUiState -> emgUiState.copy(emg2 = it) }
                    }
                }
                _emgState.value.copy()
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = _emgState.value
            )

    private val _btState = MutableStateFlow(BluetoothUiState())
    val btState = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        bluetoothController.connectedDevices,
        bluetoothController.isConnected,
        _btState
    ) { scannedDevices, pairedDevices, connectedDevices, isConnected, btState ->

        btState.copy(
            scannedDevices = scannedDevices,
            pairedDevices = pairedDevices,
            connectedDevices = connectedDevices,
            isConnected = isConnected
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _btState.value)

    private var deviceConnectionJob: Array<Job?> = arrayOf(null, null, null, null)



    init {

        bluetoothController.errors.onEach { error ->
            _btState.update { it.copy(
                errorMessage = error
            ) }
        }.launchIn(viewModelScope)
    }

    fun connectToDevice(device: BluetoothDeviceDomain, deviceNo: Int = 0) {
        _btState.update { it.copy(isConnecting = true) }

        if(!btState.value.isConnected[0]) {
            deviceConnectionJob[deviceNo] = bluetoothController
                .connectToDevice0(ConnectedDevice(name = device.name, address = device.address, assignedNo = deviceNo))
                .listen()
        } else if(!btState.value.isConnected[1]) {
            deviceConnectionJob[deviceNo] = bluetoothController
                .connectToDevice1(ConnectedDevice(name = device.name, address = device.address, assignedNo = deviceNo))
                .listen()
        } else {
            Log.d("bluetoothAddress", "이미 디바이스가 전부 연결됨")
        }
    }

    fun disconnectFromDevice(deviceNo: Int = 0) {
        deviceConnectionJob[0]?.cancel()
        deviceConnectionJob[1]?.cancel()

        viewModelScope.launch {
            bluetoothController.closeConnection(0)
            bluetoothController.closeConnection(1)
        }
//        val bufConnected = state.value.isConnected.toMutableList()
//        bufConnected[deviceNo] = false

        _btState.update { it.copy(
            isConnecting = false,
//            isConnected = bufConnected.toList()
        ) }
    }

    fun startScan() {
        bluetoothController.startDiscovery()
    }

    fun stopScan() {
        bluetoothController.stopDiscovery()
    }

    @JvmName("connection0")
    private fun Flow<Connection0Result>.listen(): Job {
        return onEach { result ->
            when(result) {
                is Connection0Result.ConnectionEstablished -> {
                    _btState.update {it.copy(
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is Connection0Result.TransferSucceeded -> {
//                    Log.d("bluetooth", "${result.message}")
//                    Log.d("bluetooth", "${state.value.isConnected}")
                    saveData(result.message)
                }
                is Connection0Result.Error -> {
                    if(result.message != "errString") {
                        _btState.update { it.copy(
                            isConnecting = false,
                            errorMessage = result.message
                        ) }
                    }
                }
            }
        }.catch {throwable ->
            Log.d("bluetooth", "종료조건 ${throwable}")
            bluetoothController.closeConnection(0)
            _btState.update { it.copy(
                isConnecting = false,
            ) }
        }.launchIn(viewModelScope)
    }

    @JvmName("connection1")
    private fun Flow<Connection1Result>.listen(): Job {
        return onEach { result ->
            when(result) {
                is Connection1Result.ConnectionEstablished -> {
                    _btState.update {it.copy(
                        isConnecting = false,
                        errorMessage = null
                    ) }
                }
                is Connection1Result.TransferSucceeded -> {
//                    Log.d("bluetooth", "${result.message}")
//                    Log.d("bluetooth", "${state.value.isConnected}")
                    if(btState.value.subscribe)
                    {
                        saveData(result.message)
                    }
                }
                is Connection1Result.Error -> {
                    if(result.message != "errString") {
                        _btState.update { it.copy(
                            isConnecting = false,
                            errorMessage = result.message
                        ) }
                    }
                }
            }
        }.catch {throwable ->
            Log.d("bluetooth", "종료조건 ${throwable}")
            bluetoothController.closeConnection(1)
            _btState.update { it.copy(
                isConnecting = false,
            ) }
        }.launchIn(viewModelScope)
    }

    suspend fun saveData(msg: BluetoothMessage) {
        val emgInput = Emg(0, msg.sensorId, "unknown", msg.message, System.currentTimeMillis())
        emgRepository.insertEmg(emgInput)
    }

    suspend fun deleteEmgData() {
        emgRepository.deleteEmgData()
    }

    fun subscribe() {
        _btState.update { bluetoothUiState -> bluetoothUiState.copy(subscribe = true) }
    }

    fun unsubscribe() {
        _btState.update { bluetoothUiState -> bluetoothUiState.copy(subscribe = false) }
    }

    fun showBottomSheet() {
        _routineState.update { routineState -> routineState.copy(showBottomSheet = true) }
    }
    fun hideBottomSheet() {
        _routineState.update { routineState -> routineState.copy(showBottomSheet = false) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            bluetoothController.release()
            emgRepository.deleteEmgData()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}