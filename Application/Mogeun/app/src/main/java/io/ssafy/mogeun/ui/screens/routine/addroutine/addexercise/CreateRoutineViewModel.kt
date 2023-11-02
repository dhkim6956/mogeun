package io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise

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
import io.ssafy.mogeun.data.CreateRoutineRepository
import io.ssafy.mogeun.model.CreateRoutineResponse
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.ui.screens.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CreateRoutineViewModel(private val createRoutineRepository: CreateRoutineRepository) : ViewModel() {
    private val _createRoutineSuccess = MutableStateFlow(false)
    val createRoutineSuccess: StateFlow<Boolean> = _createRoutineSuccess.asStateFlow()

    var text1 by mutableStateOf("")

    fun updateText1(value: String) {
        text1 = value
    }

    fun createRoutine(email: String, routineName: String) {
        lateinit var ret: CreateRoutineResponse
        viewModelScope.launch {
            ret = createRoutineRepository.createRoutine(email, routineName)
            //Log.d("jhk", "${ret.message}")
            if (ret.message == "SUCCESS") {
                _createRoutineSuccess.value = true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val createRoutineRepository = application.container.exerciseDataRepository
                CreateRoutineViewModel(createRoutineRepository = createRoutineRepository)
            }
        }
    }
}