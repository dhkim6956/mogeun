package io.ssafy.mogeun.ui.screens.setting.user

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
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.GetInbodyResponse
import io.ssafy.mogeun.model.SignUpResponse
import io.ssafy.mogeun.model.UpdateUserResponse
import io.ssafy.mogeun.ui.screens.signup.SignupViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserViewModel(
    private val UserRepository: UserRepository,
    private val keyRepository: KeyRepository,
): ViewModel() {
    var userKey by mutableStateOf<Int?>(null)
    var nickname by mutableStateOf<String?>(null)
    var height by mutableStateOf<Double?>(null)
    var weight by mutableStateOf<Double?>(null)
    var muscleMass by mutableStateOf<Double?>(null)
    var bodyFat by mutableStateOf<Double?>(null)

    fun updateUserKey(value: Int?) {
        userKey = value
    }
    fun updateNickname(value: String?) {
        nickname = value
    }
    fun updateHeight(value: Double?) {
        height = value
    }
    fun updateWeight(value: Double?) {
        weight = value
    }
    fun updateMuscleMass(value: Double?) {
        muscleMass = value
    }
    fun updateBodyFat(value: Double?) {
        bodyFat = value
    }
    fun getInbody() {
        lateinit var ret: GetInbodyResponse
        viewModelScope.launch {
            ret = UserRepository.getInbody(userKey.toString())
            Log.d("getInbody", "$ret")
            updateMuscleMass(ret.data.muscleMass)
            updateBodyFat(ret.data.bodyFat)
            updateNickname(ret.data.userName)
            updateHeight(ret.data.height)
            updateWeight(ret.data.weight)
        }
    }
    fun updateUser() {
        lateinit var ret: UpdateUserResponse
        viewModelScope.launch {
            ret = UserRepository.updateUser(
                userKey,
                nickname,
                height,
                weight,
                muscleMass,
                bodyFat
            )
            Log.d("updateUser", "$ret")
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
                val userRepository = application.container.userDataRepository
                val keyRepository = application.container.keyRepository
                UserViewModel(UserRepository = userRepository, keyRepository)
            }
        }
    }
}