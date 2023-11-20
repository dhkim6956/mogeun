package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.ui.barchart.models.BarData
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RecordRepository
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.MonthlyRoutine
import io.ssafy.mogeun.model.RoutineInfoData
import io.ssafy.mogeun.model.RoutineResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RecordViewModel(
    private val recordRepository: RecordRepository,
    private val keyRepository: KeyRepository
): ViewModel() {
    private val _recordMonthlySuccess = MutableStateFlow(false)
    val recordMonthlySuccess: StateFlow<Boolean> = _recordMonthlySuccess.asStateFlow()
    var recordList = mutableStateListOf<MonthlyRoutine>()

    private val _recordAllRoutineSuccess = MutableStateFlow(false)
    private val _recordAllRoutineLoading = MutableStateFlow(false)
    val recordAllRoutineSuccess: StateFlow<Boolean> = _recordAllRoutineSuccess.asStateFlow()
    val recordAllRoutineLoading: StateFlow<Boolean> = _recordAllRoutineLoading.asStateFlow()
    var routineInfoMap = mutableStateMapOf<String, RoutineInfoData>()

    private val _recordRoutineSuccess = MutableStateFlow(false)
    val recordRoutineSuccess: StateFlow<Boolean> = _recordRoutineSuccess.asStateFlow()
    var routineInfo by mutableStateOf<RoutineInfoData?>(null)

    var userKey by mutableStateOf<Int?>(null)

    var itemIndex = mutableIntStateOf(0)

    fun initRecordMonthlySuccess() {
        _recordMonthlySuccess.value = false
        recordList.clear()
    }

    private fun getUserKey() {
        viewModelScope.launch(Dispatchers.IO) {
            val key = keyRepository.getKey()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            launch(Dispatchers.Main) {
                updateUserKey(userKey)
            }
        }
    }

    fun recordMonthly(date: String) {
        // Get userKey
        getUserKey()

        if (userKey !== null) {
            lateinit var ret: MonthlyResponse
            viewModelScope.launch {
                ret = recordRepository.recordMonthly(userKey.toString(), date)
                Log.d("recordMonthly", "$ret")

                if (ret.status == "OK") {
                    _recordMonthlySuccess.value = true
                    for (record in ret.data) {
                        recordList.add(record)
                    }
                }
            }
        }
    }

    fun updateRecordAllRoutineSuccess() {
        _recordAllRoutineSuccess.value = true
    }

    fun recordAllRoutine(reportKeyList: List<Int>) {
        getUserKey()

        if (userKey !== null) {
            _recordAllRoutineLoading.value = true
            lateinit var ret: RoutineResponse
            viewModelScope.launch {
                Log.d("requestInfo", "userKey : ${userKey}")
                for (reportKey in reportKeyList!!) {
                    ret = recordRepository.recordRoutine(userKey.toString(), reportKey.toString())
                    Log.d("recordRoutine", "$ret")
                    if (ret.status == "OK") {
                        routineInfoMap[reportKey.toString()] = ret.data!!
                    }
                }
            }
        }
    }

    fun recordRoutine(reportKey: String) {
        getUserKey()

        if (userKey !== null) {
            lateinit var ret: RoutineResponse
            viewModelScope.launch {
                ret = recordRepository.recordRoutine(userKey.toString(), reportKey)
                Log.d("recordRoutine", "$ret")
                if (ret.status == "OK") {
                    _recordRoutineSuccess.value = true
                    routineInfo = ret.data
                }
            }
        }
    }

    private fun updateUserKey(update: Int?) {
        userKey= update
    }
}