package io.ssafy.mogeun.ui.screens.record

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.RecordRepository
import io.ssafy.mogeun.model.MonthlyResponse
import io.ssafy.mogeun.model.RoutineInfoList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class RecordViewModel(private val recordRepository: RecordRepository): ViewModel() {
    private val _recordMonthlySuccess = MutableStateFlow(false)
    val recordMonthlySuccess: StateFlow<Boolean> = _recordMonthlySuccess.asStateFlow()
    var recordList = mutableStateListOf<RoutineInfoList>()

    fun initRecordMonthlySuccess() {
        _recordMonthlySuccess.value = false
    }

    fun recordMonthly(userKey: String, date: String) {
        lateinit var ret: MonthlyResponse
        viewModelScope.launch {
            ret = recordRepository.recordMonthly(userKey, date)
            Log.d("recordMonthly", "$ret")

            if(ret.message == "SUCCESS") {
                _recordMonthlySuccess.value = true
                for (record in ret.data) {
                    recordList.add(record)
                }
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val recordRepository = application.container.recordRepository
                RecordViewModel(recordRepository = recordRepository)
            }
        }
    }
}