package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
    var routineInfoList = mutableStateListOf<RoutineInfoData>()

    var userKey by mutableStateOf<Int?>(null)

    var barChartdata: MutableList<BarData> = mutableListOf()

    fun initRecordMonthlySuccess() {
        _recordMonthlySuccess.value = false
        recordList.clear()
    }

    private fun getUserKey() {
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
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

    fun recordRoutine(reportKeyList: List<Int>) {
        getUserKey()

        if (userKey !== null) {
            lateinit var ret: RoutineResponse
            viewModelScope.launch {
                for (reportKey in reportKeyList) {
                    ret = recordRepository.recordRoutine(userKey.toString(), reportKey.toString())
                    Log.d("recordRoutine", "$ret")

                    if (ret.status == "OK") {
                        routineInfoList.add(ret.data)
                    }
                }
                _recordRoutineSuccess.value = true
            }
        }
    }

    private fun updateUserKey(update: Int?) {
        userKey= update
    }
}