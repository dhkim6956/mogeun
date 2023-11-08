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
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.model.AddRoutineResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddRoutineViewModel(private val addRoutineRepository: RoutineRepository) : ViewModel() {
    private val _addRoutineSuccess = MutableStateFlow(false)
    val addRoutineSuccess: StateFlow<Boolean> = _addRoutineSuccess.asStateFlow()

    var text1 by mutableStateOf("")

    fun updateText1(value: String) {
        text1 = value
    }

    fun addRoutine(userKey: Int, routineName: String) {
        lateinit var ret: AddRoutineResponse
        viewModelScope.launch {
            ret = addRoutineRepository.addRoutine(userKey, routineName)
            Log.d("addroutine", "$ret")
            if (ret.message == "SUCCESS") {
                _addRoutineSuccess.value = true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val addRoutineRepository = application.container.addRoutineRepository
                AddRoutineViewModel(addRoutineRepository = addRoutineRepository)
            }
        }
    }
}