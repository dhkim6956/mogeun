package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ssafy.mogeun.data.RecordRepository
import io.ssafy.mogeun.model.RoutineInfoData
import io.ssafy.mogeun.model.RoutineResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecordDetailViewModel(private val recordRepository: RecordRepository): ViewModel() {
    private val _recordRoutineSuccess = MutableStateFlow(false)
    val recordRoutineSuccess: StateFlow<Boolean> = _recordRoutineSuccess.asStateFlow()
    var routineInfo by mutableStateOf<RoutineInfoData?>(null)

    fun initRecordRoutineSuccess() {
        _recordRoutineSuccess.value = false
    }

    fun recordRoutine(userKey: String, reportKey: String) {
        lateinit var ret: RoutineResponse
        viewModelScope.launch {
            ret = recordRepository.recordRoutine(userKey, reportKey)
            Log.d("recordMonthly", "$ret")

            if(ret.message == "SUCCESS") {
                _recordRoutineSuccess.value = true
                routineInfo = ret.data
            }
        }
    }
}