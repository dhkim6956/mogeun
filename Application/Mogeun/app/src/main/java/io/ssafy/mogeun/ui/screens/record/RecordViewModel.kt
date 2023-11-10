package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import co.yml.charts.ui.barchart.models.BarData
import com.patrykandpatrick.vico.core.extension.orZero
import io.ssafy.mogeun.data.Key
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RecordRepository
import io.ssafy.mogeun.model.Exercise
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.MonthlyRoutine
import io.ssafy.mogeun.model.RoutineInfoData
import io.ssafy.mogeun.model.RoutineResponse
import kotlinx.coroutines.flow.Flow
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

    private val _recordRoutineSuccess = MutableStateFlow(false)
    val recordRoutineSuccess: StateFlow<Boolean> = _recordRoutineSuccess.asStateFlow()
    var routineInfo by mutableStateOf<RoutineInfoData?>(null)

    var userKey by mutableStateOf<Int?>(null)

    var barChartdata: MutableList<BarData> = mutableListOf()

    fun initRecordMonthlySuccess() {
        _recordMonthlySuccess.value = false
        recordList.clear()
    }

    fun recordMonthly(date: String) {
        // Get userKey
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
        }

        if (userKey !== null) {
            lateinit var ret: MonthlyResponse
            viewModelScope.launch {
                ret = recordRepository.recordMonthly(userKey.toString(), date)
                Log.d("recordMonthly", "$ret")

                if(ret.message == "SUCCESS") {
                    _recordMonthlySuccess.value = true
                    for (record in ret.data) {
                        recordList.add(record)
                    }
                }
            }
        }
    }

    fun recordRoutine(reportKey: String) {
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
        }

        if (userKey !== null) {
            lateinit var ret: RoutineResponse
            viewModelScope.launch {
                ret = recordRepository.recordRoutine(userKey.toString(), reportKey)
                Log.d("recordRoutine", "$ret")

                if (ret.message == "SUCCESS") {
                    _recordRoutineSuccess.value = true
                    routineInfo = ret.data
                }

//                updateChartData(routineInfo!!.exercises)
            }
        }
    }

    private fun updateUserKey(update: Int?) {
        userKey= update
    }

    private fun updateChartData(exercises: List<Exercise>) {
        var map: MutableMap<String, Float> = mutableMapOf()
        var index = 0
        for (exercise in exercises) {
            for (part in exercise.parts) {
                val partDetail = part.split(" ")
                if (partDetail[0] == "주") {
                    if (map.containsKey(partDetail[1])) map[partDetail[1]] = map[partDetail[1]]!!.plus(2f)
                    else map.put(partDetail[1], 2f)
                }
                else {
                    if (map.containsKey(partDetail[1])) map[partDetail[1]] = map[partDetail[1]]!!.plus(1f)
                    else map.put(partDetail[1], 1f)
                }
            }
            index++
        }

        for (data in map) {
            barChartdata.add(index, BarData(Point(index.toFloat(), data.value), Color.Red, data.key))
            index++
        }
    }
}