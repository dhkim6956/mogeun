package io.ssafy.mogeun.ui.screens.routine.searchRoutine

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.GetRoutineListResponse
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class RoutineViewModel(
    private val UserRepository: UserRepository,
    private val keyRepository: KeyRepository,
    private val RoutineRepository: RoutineRepository
) : ViewModel() {
    var muscleMass by mutableStateOf<Double?>(null)
    var bodyFat by mutableStateOf<Double?>(null)
    var userKey by mutableStateOf<Int?>(null)
    var tmp by mutableStateOf<GetRoutineListResponse?>(null)
    var username by mutableStateOf<String?>(null)

    fun updateMuscleMass(value: Double?) {
        muscleMass = value
    }
    fun updateBodyFat(value: Double?) {
        bodyFat = value
    }
    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun updateUsername(value: String?) {
        username = value
    }
    fun getInbody() {
        lateinit var ret: GetInbodyResponse
        viewModelScope.launch {
            ret = UserRepository.getInbody(userKey.toString())
            Log.d("getInbody", "$ret")
            updateMuscleMass(ret.data.muscleMass)
            updateBodyFat(ret.data.bodyFat)
            updateUsername(ret.data.userName)
            Log.d("updateUserKey", "${userKey}")
        }
    }
    fun getRoutineList() {
        lateinit var ret: GetRoutineListResponse
        viewModelScope.launch {
            ret = RoutineRepository.getRoutineList(userKey.toString())
            Log.d("RoutineList", "$ret")
            tmp = ret
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
                val UserRepository = application.container.userDataRepository
                val keyRepository = application.container.keyRepository
                val RoutineRepository = application.container.addRoutineRepository
                RoutineViewModel(UserRepository = UserRepository ,keyRepository, RoutineRepository = RoutineRepository)
            }
        }
    }
}