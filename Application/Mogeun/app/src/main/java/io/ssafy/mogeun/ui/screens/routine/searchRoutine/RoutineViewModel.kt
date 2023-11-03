package io.ssafy.mogeun.ui.screens.routine.searchRoutine

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
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel
import kotlinx.coroutines.launch

class RoutineViewModel(private val UserRepository: UserRepository): ViewModel() {
    var muscleMass by mutableStateOf<Double?>(null)
    var bodyFat by mutableStateOf<Double?>(null)

    fun updateMuscleMass(value: Double?) {
        muscleMass = value
    }
    fun updateBodyFat(value: Double?) {
        bodyFat = value
    }

    fun getInbody() {
        lateinit var ret: GetInbodyResponse
        viewModelScope.launch {
            ret = UserRepository.getInbody("11")
            Log.d("getInbody", "$ret")
            updateMuscleMass(ret.data.muscleMass)
            updateBodyFat(ret.data.bodyFat)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val UserRepository = application.container.userDataRepository
                RoutineViewModel(UserRepository = UserRepository)
            }
        }
    }
}