package io.ssafy.mogeun.ui.screens.signup

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.SignInRepository
import io.ssafy.mogeun.model.DupEmailResponse
import io.ssafy.mogeun.model.SignInResponse
import io.ssafy.mogeun.ui.screens.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SignupViewModel(private val signInRepository: SignInRepository): ViewModel() {
    private val _dupEmailSuccess = MutableStateFlow(false)
    val dupEmailSuccess: StateFlow<Boolean> = _dupEmailSuccess.asStateFlow()
    var id by mutableStateOf("")
    var password by mutableStateOf("")
    var checkingPassword by mutableStateOf("")
    var nickname by mutableStateOf("")
    var height by mutableStateOf<Float?>(null)
    var weight by mutableStateOf<Float?>(null)
    var muscleMass by mutableStateOf<Float?>(null)
    var bodyFat by mutableStateOf<Float?>(null)
    var inputForm by mutableIntStateOf(1)
    var firstText by mutableStateOf("회원정보를")

    fun updateId(value: String) {
        id = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updateCheckingPassword(value: String) {
        checkingPassword = value
    }

    fun updateNickname(value: String) {
        nickname = value
    }
    fun updateHeight(value: Float) {
        height = value
    }

    fun updateWeight(value: Float) {
        weight = value
    }

    fun updateMuscleMass(value: Float) {
        muscleMass = value
    }

    fun updateBodyFat(value: Float) {
        bodyFat = value
    }

    fun updateInputForm(value: Int) {
        inputForm = value
    }

    fun updateFirstText(value: String) {
        firstText = value
    }

    fun dupEmail(email: String) {
        lateinit var ret: DupEmailResponse
        viewModelScope.launch {
            ret = signInRepository.dupEmail(email)
            Log.d("dupEmail", "$ret")

            if(ret.message == "SUCCESS") {
                _dupEmailSuccess.value = true
            }
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val signInRepository = application.container.userDataRepository
                SignupViewModel(signInRepository = signInRepository)
            }
        }
    }
}