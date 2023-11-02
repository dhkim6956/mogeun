package io.ssafy.mogeun.ui.screens.login

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.SignInRepository
import io.ssafy.mogeun.model.SignInResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val signInRepository: SignInRepository) : ViewModel() {
    private val _signInSuccess = MutableStateFlow(false)
    val signInSuccess: StateFlow<Boolean> = _signInSuccess.asStateFlow()

    // 텍스트 필드에 대한 상태 변수
    var text1 by mutableStateOf("")
    var text2 by mutableStateOf("")

    // text1 및 text2를 업데이트하는 함수
    fun updateText1(value: String) {
        text1 = value
    }

    fun updateText2(value: String) {
        text2 = value
    }

    fun signIn(email: String, pw: String) {
        lateinit var ret: SignInResponse
        viewModelScope.launch {
            ret = signInRepository.signIn(email, pw)

            if (ret.message == "SUCCESS") {
                _signInSuccess.value = true
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MogeunApplication)
                val signInRepository = application.container.userDataRepository
                LoginViewModel(signInRepository = signInRepository)
            }
        }
    }
}
