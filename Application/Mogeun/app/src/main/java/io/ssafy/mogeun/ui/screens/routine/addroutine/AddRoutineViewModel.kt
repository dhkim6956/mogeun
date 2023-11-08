package io.ssafy.mogeun.ui.screens.routine.addroutine

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.AddRoutineResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import androidx.compose.runtime.collectAsState

class AddRoutineViewModel(
    private val addRoutineRepository: RoutineRepository,
    private val keyRepository: KeyRepository
) : ViewModel() {
    var userKey by mutableStateOf<Int?>(null)
    private val _addRoutineSuccess = MutableStateFlow(false)
    val addRoutineSuccess: StateFlow<Boolean> = _addRoutineSuccess.asStateFlow()

    var text1 by mutableStateOf("")

    fun updateText1(value: String) {
        text1 = value
    }
    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun addRoutine(userKey: Int?, routineName: String) {
        lateinit var ret: AddRoutineResponse
        viewModelScope.launch {
            ret = addRoutineRepository.addRoutine(userKey, routineName)
            Log.d("addroutine", "$ret")
            if (ret.message == "SUCCESS") {
                _addRoutineSuccess.value = true
            }
        }
    }
    fun getUserKey() {
        viewModelScope.launch {
            val key = keyRepository.getKey().first()
            val userKey = key?.userKey
            Log.d("getUserKey", "사용자 키: $userKey")
            updateUserKey(userKey)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val addRoutineRepository = application.container.addRoutineRepository
                val keyRepository = application.container.keyRepository
                AddRoutineViewModel(addRoutineRepository = addRoutineRepository, keyRepository)
            }
        }
    }
}