package io.ssafy.mogeun.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.ExecutionRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.data.bluetooth.BluetoothController
import io.ssafy.mogeun.data.bluetooth.BluetoothDeviceDomain
import io.ssafy.mogeun.data.bluetooth.ConnectedDevice
import io.ssafy.mogeun.data.bluetooth.Connection0Result
import io.ssafy.mogeun.data.bluetooth.Connection1Result
import io.ssafy.mogeun.model.BluetoothMessage
import io.ssafy.mogeun.model.SetOfRoutineDetail
import io.ssafy.mogeun.model.SetOfRoutineResponse
import io.ssafy.mogeun.model.SetOfRoutineResponseData
import io.ssafy.mogeun.ui.screens.routine.execution.ElapsedTime
import io.ssafy.mogeun.ui.screens.routine.execution.EmgUiState
import io.ssafy.mogeun.ui.screens.routine.execution.RoutineState
import io.ssafy.mogeun.ui.screens.routine.execution.SetOfPlan
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
import kotlin.math.abs


class BluetoothViewModel(
    private val executionRepository: ExecutionRepository,
    private val emgRepository: EmgRepository,
    private val routineRepository: RoutineRepository,
    private val bluetoothController: BluetoothController
): ViewModel() {
    private val _routineState = MutableStateFlow(RoutineState(null, showBottomSheet = false))
    val routineState = _routineState.asStateFlow()

    fun getPlanList(routineKey: Int) {
        viewModelScope.launch {
            val ret = routineRepository.listMyExercise(routineKey)
            _routineState.update { routineState -> routineState.copy(planList = ret) }
        }
    }

    fun getSetOfRoutine() {
        _routineState.update { routineState -> routineState.copy(planDetailsRequested = true) }

        viewModelScope.launch {
            routineState.value.planList!!.data.forEach { plan ->
                val planKey = plan.planKey

                val ret = executionRepository.getSetOfRoutine(planKey)
                if(ret.data == null)
                {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, true, listOf(
                        SetOfRoutineDetail(1, 55, 10),
                        SetOfRoutineDetail(2, 60, 8),
                        SetOfRoutineDetail(3, 65, 6)
                    ))) }
                } else {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, false, ret.data.setDetails)) }
                }
            }
        }
    }

    fun addSet(planKey: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail + setOfPlan.setOfRoutineDetail.last().copy(setNumber = setOfPlan.setOfRoutineDetail.size + 1))
                } else {
                    setOfPlan
                }
            }
            Log.d("update", "${changedPlanDetails[0].setOfRoutineDetail}")

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun removeSet(planKey: Int, setIdx: Int) {
        Log.d("update", "removeSet : $planKey")
        _routineState.update { routineState ->
            var newIdx = 1
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey && setOfPlan.setOfRoutineDetail.size > 1) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.filter { setOfRoutineDetail -> setOfRoutineDetail.setNumber != setIdx }.map { setOfRoutineDetail -> setOfRoutineDetail.copy(setNumber = newIdx++) })
                } else {
                    setOfPlan
                }
            }
            Log.d("update", "${changedPlanDetails[0].setOfRoutineDetail}")

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    val _elaspedTime = MutableStateFlow(ElapsedTime(System.currentTimeMillis(), 0, 0))
    val elaspedTime = _elaspedTime.asStateFlow()

    var terminateTimer by mutableStateOf(false)

    suspend fun runTimer() {
        _elaspedTime.update { elapsedTime -> elapsedTime.copy(startTime = System.currentTimeMillis()) }
        while (!terminateTimer) {
            delay(1000)
            val now = System.currentTimeMillis()
            val offset = (now - elaspedTime.value.startTime).toInt() / 1000
            _elaspedTime.update { elapsedTime -> elapsedTime.copy(minute = offset / 60, second = offset % 60) }
        }
    }

    private val _emgState = MutableStateFlow(EmgUiState())
    val emgState = _emgState.asStateFlow()
//    val emgState: StateFlow<EmgUiState> =
//        emgRepository.getEmgStream()
//            .map {
//                if(it != null) {
//                    if(it.deviceId == 0) {
//                        _emgState.update { emgUiState ->
//                            val bufValue = abs(it.value)
//                            val bufList = if(emgUiState.emg1List.size < 80) emgUiState.emg1List + bufValue else emgUiState.emg1List.subList(1, 80) + bufValue
//                            emgUiState.copy(
//                                emg1 = it,
//                                emg1List = bufList,
//                                emg1Avg = bufList.average()
//                        ) }
//
//                    } else {
//                        _emgState.update { emgUiState ->
//                            val bufValue = abs(it.value)
//                            val bufList = if(emgUiState.emg2List.size < 80) emgUiState.emg2List + bufValue else emgUiState.emg2List.subList(1, 80) + bufValue
//                            emgUiState.copy(
//                                emg2 = it,
//                                emg2List = bufList,
//                                emg2Avg = bufList.average()
//                            ) }
//                    }
//                }
//                _emgState.value.copy()
//            }.stateIn(
//                viewModelScope,
//                SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = _emgState.value
//            )

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


        viewModelScope.launch {
            runTimer()
        }
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

                    if(result.message.sensorId == 0) {
                        _emgState.update { emgUiState ->
                            val bufValue = abs(result.message.message)
                            val bufList = if(bufValue > 1500) emgUiState.emg1List else {if(emgUiState.emg1List.size < 80) emgUiState.emg1List + bufValue else emgUiState.emg1List.subList(1, 80) + bufValue}
                            val avg = bufList.average()
                            emgUiState.copy(
                                emg1 = Emg(0, result.message.sensorId, "unknown", result.message.message, System.currentTimeMillis()),
                                emg1List = bufList,
                                emg1Avg = avg,
                                emg1Max = if(emgUiState.emg1Max < avg) avg.toInt() else emgUiState.emg1Max
                            )
                        }
                    }
                    viewModelScope.launch {
                        saveData(result.message)
                    }
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
                    if(result.message.sensorId == 1) {
                        _emgState.update { emgUiState ->
                            val bufValue = abs(result.message.message)
                            val bufList = if(bufValue > 1500) emgUiState.emg2List else {if(emgUiState.emg2List.size < 80) emgUiState.emg2List + bufValue else emgUiState.emg2List.subList(1, 80) + bufValue}
                            val avg = bufList.average()
                            emgUiState.copy(
                                emg2 = Emg(0, result.message.sensorId, "unknown", result.message.message, System.currentTimeMillis()),
                                emg2List = bufList,
                                emg2Avg = avg,
                                emg2Max = if(emgUiState.emg2Max < avg) avg.toInt() else emgUiState.emg2Max
                            )
                        }
                    }
                    viewModelScope.launch {
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