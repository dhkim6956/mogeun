package io.ssafy.mogeun.ui.screens.routine.execution

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.extensions.isNotNull
import io.ssafy.mogeun.data.BleRepository
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.ExecutionRepository
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.SensorData
import io.ssafy.mogeun.model.SetExecutionRequest
import io.ssafy.mogeun.model.SetInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jtransforms.fft.DoubleFFT_1D
import java.text.SimpleDateFormat
import kotlin.math.abs


class ExecutionViewModel(
    private val executionRepository: ExecutionRepository,
    private val emgRepository: EmgRepository,
    private val routineRepository: RoutineRepository,
    private val bleRepository: BleRepository,
    private val keyRepository: KeyRepository,
): ViewModel() {

    var userKey by mutableStateOf<Int?>(null)
    fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            Log.d("execution", "사용자 키: $userKey")
            launch(Dispatchers.Main) {
                userKey = key?.userKey
            }
        }
    }

    private val _routineState = MutableStateFlow(RoutineState(null, showBottomSheet = false))
    val routineState = _routineState.asStateFlow()


    suspend fun getPlanList(routineKey: Int) {
        val ret = routineRepository.listMyExercise(routineKey)
        _routineState.update { routineState -> routineState.copy(planList = ret) }
    }

    fun getSetOfRoutine() {
        viewModelScope.launch {
            routineState.value.planList!!.data.forEach { plan ->
                val planKey = plan.planKey

                val ret = executionRepository.getSetOfRoutine(planKey)
                if(ret.data == null)
                {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, true, listOf(
                        SetProgress(1, 55, 10),
                        SetProgress(2, 60, 8),
                        SetProgress(3, 65, 6)
                    )
                        )) }
                } else {
                    _routineState.update { routineState -> routineState.copy(planDetails = routineState.planDetails + SetOfPlan(planKey, false, ret.data.setDetails.map { SetProgress(it.setNumber, it.weight, it.targetRep) }) ) }
                }
            }
        }
    }

    fun addSet(planKey: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail + setOfPlan.setOfRoutineDetail.last().copy(setNumber = setOfPlan.setOfRoutineDetail.size + 1, successRep = 0))
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun removeSet(planKey: Int, setIdx: Int) {
        _routineState.update { routineState ->
            var newIdx = 1
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if (setOfPlan.planKey == planKey && setOfPlan.setOfRoutineDetail.size > 1) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.filter { setOfRoutineDetail -> setOfRoutineDetail.setNumber != setIdx }.map { setOfRoutineDetail -> setOfRoutineDetail.copy(setNumber = newIdx++) })
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun setWeight(planKey: Int, setIdx: Int, weight: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if(setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setOfRoutineDetail -> if(setOfRoutineDetail.setNumber == setIdx) setOfRoutineDetail.copy(targetWeight = weight) else setOfRoutineDetail } )
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun setRep(planKey: Int, setIdx: Int, rep: Int) {
        _routineState.update { routineState ->
            val changedPlanDetails = routineState.planDetails.map { setOfPlan ->
                if(setOfPlan.planKey == planKey) {
                    setOfPlan.copy(valueChanged = true, setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setOfRoutineDetail -> if(setOfRoutineDetail.setNumber == setIdx) setOfRoutineDetail.copy(targetRep = rep) else setOfRoutineDetail } )
                } else {
                    setOfPlan
                }
            }

            routineState.copy(planDetails = changedPlanDetails)
        }
    }

    fun startSet(planKey: Int, setIdx: Int) {
        if(routineState.value.setInProgress) return

        _routineState.update { routineState -> routineState.copy(setInProgress = true, planDetails = routineState.planDetails.map { setOfPlan ->
            if(setOfPlan.planKey == planKey) {
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { routineDetail ->
                    if(routineDetail.setNumber == setIdx) {
                        routineDetail.copy(startTime = System.currentTimeMillis())
                    } else {
                        routineDetail
                    }
                })
            } else {
                setOfPlan
            }
        }) }
    }

    fun addCnt(planKey: Int, setIdx: Int) {
        _routineState.update { routineState -> routineState.copy(hasValidSet = true, planDetails = routineState.planDetails.map { setOfPlan ->
            if(setOfPlan.planKey == planKey) {
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { routineDetail ->
                    if(routineDetail.setNumber == setIdx) {
                        routineDetail.copy(successRep = routineDetail.successRep + 1)
                    } else {
                        routineDetail
                    }
                })
            } else {
                setOfPlan
            }
        }) }

        Log.d("set", "cnt+ : ${routineState.value}")
    }


    private val _muscleavg = MutableStateFlow(0.0)
    val muscleavg = _muscleavg.asStateFlow()

    fun FFT_ready(emgList: List<Int>): Double {//N은 신호의 갯수
        val N = emgList.size

        val a = DoubleArray(2 * N) //fft 수행할 배열 사이즈 2N

        for (k in 0 until N) {
            a[2 * k] = emgList[k].toDouble() //Re
            a[2 * k + 1] = 0.0 //Im
        }

        val fft = DoubleFFT_1D(N.toLong()) //1차원의 fft 수행

        fft.complexForward(a) //a 배열에 output overwrite


        val mag = DoubleArray(N / 2)
        var sum = 0.0
        for (k in 0 until N / 2) {
            mag[k] = Math.sqrt(Math.pow(a[2 * k], 2.0) + Math.pow(a[2 * k + 1], 2.0))
            sum += mag[k]
        }
        var average = 0.0
        var nowSum = 0.0
        var fatigue = 0
        for (k in 0 until N / 2) {
            nowSum += mag[k];
            average = nowSum / sum

            if (average >= 0.5) {
                fatigue = k
                break
            }
        }


        return fatigue.toDouble() * 1000 / N
    }

    fun endSet(planKey: Int, setIdx: Int) {
        if(!routineState.value.setInProgress) return

        _routineState.update { routineState -> routineState.copy(setInProgress = false) }

        val reportKey = routineState.value.reportKey
        val plan = routineState.value.planDetails.find { setOfPlan -> setOfPlan.planKey == planKey }

        if (!plan.isNotNull()) return

        val set = plan!!.setOfRoutineDetail.find { setProgress -> setProgress.setNumber == setIdx }

        if (!set.isNotNull()) return

        val targetWeight = set!!.targetWeight
        val targetRep = set!!.targetRep
        val successRep = set!!.successRep

        val startTime: Long = set!!.startTime!!
        val endTime: Long = System.currentTimeMillis()

        val dateFormat = "yyyy-MM-dd"
        val timeFormat = "HH:mm:ss"
        val dateFormatter = SimpleDateFormat(dateFormat)
        val timeFormatter = SimpleDateFormat(timeFormat)

        val formmattedStartDate: String = dateFormatter.format(startTime)
        val formmattedStartTime: String = timeFormatter.format(startTime)
        val formmattedEndDate: String = dateFormatter.format(endTime)
        val formmattedEndTime: String = timeFormatter.format(endTime)

        viewModelScope.launch {
            CoroutineScope(Dispatchers.IO).launch {
                val entireEmgList = emgRepository.getEmgData(startTime, endTime)

                var fatigues = mutableListOf(0.0, 0.0)
                var averages = mutableListOf(0.0, 0.0)

                for (i in 0..1) {
                    val setEmgList = entireEmgList.filter { it.deviceId == i }.map { it.value }

                    if (setEmgList.isEmpty()) break

                    fatigues[i] = FFT_ready(setEmgList)
                    _muscleavg.update { fatigues[i] }

                    val setEmgListAbs = setEmgList.map { abs(it) }
                    averages[i] = setEmgListAbs.average()
                }

                _routineState.update { routineState ->
                    routineState.copy(planDetails = routineState.planDetails.map {setOfPlan ->
                        if (setOfPlan.planKey == planKey) {
                            setOfPlan.copy(
                                setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map {setProgress ->
                                    if(setProgress.setNumber == setIdx) {
                                        setProgress.copy(sensorData = listOf(MuscleSensorValue(averages[0], fatigues[0]),MuscleSensorValue(averages[1], fatigues[1])))
                                    } else {
                                        setProgress
                                    }
                                }
                            )
                        } else {
                            setOfPlan
                        }
                    })
                }

                if (plan.valueChanged) {
                    val ret1 = async {
                        executionRepository.clearPlan(planKey)
                    }.await()

                    Log.d("report", "clear plan : $ret1")

                    val ret2 = async {
                        executionRepository.setPlan(planKey, plan.setOfRoutineDetail.map { setProgress ->
                            SetInfo(setProgress.setNumber, setProgress.targetWeight, setProgress.targetRep)
                        })
                    }.await()

                    Log.d("report", "set plan : $ret2")

                    val ret3 = executionRepository.reportSet(
                        SetExecutionRequest(
                            routineReportKey = reportKey!!,
                            planKey = planKey,
                            setNumber = setIdx,
                            weight = targetWeight,
                            targetRep = targetRep,
                            successRep = successRep,
                            startTime = "${formmattedStartDate}T${formmattedStartTime}",
                            endTime = "${formmattedEndDate}T${formmattedEndTime}",
                            muscleActs = listOf(
                                SensorData(1,averages[0],fatigues[0]),
                                SensorData(2,averages[1],fatigues[1])
                            )
                        )
                    )

                    Log.d("report", "report set : $ret3")
                }
            }
        }
    }

    var terminateTimer by mutableStateOf(true)

    suspend fun startRoutine(routineKey: Int, isAttached: String = "Y") {
        if(isAttached == "N") {
            _routineState.update { routineState -> routineState.copy(hasValidSet = true) }
        }

        terminateTimer = false
        viewModelScope.launch {
            runTimer()
        }
        _routineState.update { routineState -> routineState.copy(routineInProgress = true) }

        viewModelScope.launch {
            val ret = executionRepository.startRoutine(userKey!!, routineKey, isAttached)
            Log.d("report", "routine started = $ret")
            _routineState.update { routineState -> routineState.copy(reportKey = ret.data!!.reportKey) }
        }
    }

    fun endRoutine() {
        val reportKey = routineState.value.reportKey
        terminateTimer = true

        _routineState.update { routineState -> routineState.copy(routineInProgress = false) }

        if(routineState.value.hasValidSet) {
            viewModelScope.launch {
                val ret1 = executionRepository.endRoutine(userKey!!, reportKey!!)
                Log.d("report", "routine report = $ret1")

                val ret2 = executionRepository.reportCalorie(reportKey, 0.0)
                Log.d("report", "calorie report = $ret2")
            }
        }
    }

    fun resetRoutine() {
        _routineState.update { routineState -> routineState.copy(
            routineInProgress = false,
            setInProgress = false,
            hasValidSet = false,
            planDetails = routineState.planDetails.map { setOfPlan ->
                setOfPlan.copy(setOfRoutineDetail = setOfPlan.setOfRoutineDetail.map { setProgress ->
                    setProgress.copy(successRep = 0, sensorData = listOf(MuscleSensorValue(), MuscleSensorValue()), startTime = null)
                })
            }) }
    }

    val _elaspedTime = MutableStateFlow(ElapsedTime(System.currentTimeMillis(), 0, 0))
    val elaspedTime = _elaspedTime.asStateFlow()

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

    private val _sensorState = MutableStateFlow(SensorState())
    val sensorState: StateFlow<SensorState> = combine(
        bleRepository.connectedDevices,
        _sensorState
    ) { connectedDevices, state ->
        state.copy(
            connectedDevices = connectedDevices,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(TIMEOUT_MILLIS), _sensorState.value)

    fun getSensorVal() {
        viewModelScope.launch {
            bleRepository.sensorVal.collect { sensorVal ->
                for (i in 0..1) {
                    if(!bleRepository.connectedDevices.value[i].isNotNull())
                    {
                        _emgState.update {emgUiState ->
                            var bufList = emgUiState.emgList.toMutableList()
                            bufList[i] = listOf()
                            emgUiState.copy(
                                emgList = bufList
                            )
                        }
                    }
                    _emgState.update { emgUiState ->
                        val bufValue = abs(sensorVal[i])
                        val calcList = if(bufValue > 1500) emgUiState.emgList[i] else {if(emgUiState.emgList[i].size < 80) emgUiState.emgList[i] + bufValue else emgUiState.emgList[i].subList(1, 80) + bufValue}
                        var avg = calcList.average()
                        if(avg.isNaN()) {
                            avg = 0.0
                        }

                        var bufEmg: MutableList<Emg?> = emgUiState.emg.toMutableList()
                        bufEmg[i] = Emg(0, i, "unknown", sensorVal[i], System.currentTimeMillis())

                        var bufList = emgUiState.emgList.toMutableList()
                        bufList[i] = calcList

                        var bufAvg = emgUiState.emgAvg.toMutableList()
                        bufAvg[i] = avg

                        var bufMax = emgUiState.emgMax.toMutableList()
                        bufMax[i] = if(emgUiState.emgMax[i] < avg) avg.toInt() else emgUiState.emgMax[i]


                        emgUiState.copy(
                            emg = bufEmg,
                            emgList = bufList,
                            emgAvg = bufAvg,
                            emgMax = bufMax
                        )
                    }
                    viewModelScope.launch {
                        saveData(i, sensorVal[i])
                    }
                }
            }
        }
    }

    suspend fun saveData(idx: Int, value: Int) {
        val emgInput = Emg(0, idx, "unknown", value, System.currentTimeMillis())
        emgRepository.insertEmg(emgInput)
    }

    suspend fun deleteEmgData() {
        emgRepository.deleteEmgData()
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
            emgRepository.deleteEmgData()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}