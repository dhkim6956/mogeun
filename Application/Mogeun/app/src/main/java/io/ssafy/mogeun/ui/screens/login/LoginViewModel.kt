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
import io.ssafy.mogeun.data.Emg
import io.ssafy.mogeun.data.EmgRepository
import io.ssafy.mogeun.data.Key
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.model.SignInResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val signInRepository: UserRepository,
    private val keyRepository: KeyRepository
) : ViewModel() {
    private val _keyInput = mutableStateOf<Key>(Key(0, 1))
    val keyInput = _keyInput

    private val _signInSuccess = MutableStateFlow(false)
    val signInSuccess: StateFlow<Boolean> = _signInSuccess.asStateFlow()
    // 텍스트 필드에 대한 상태 변수
    var id by mutableStateOf("")
    var pwd by mutableStateOf("")

    // text1 및 text2를 업데이트하는 함수
    fun updateText1(value: String) {
        id = value
    }

    fun updateText2(value: String) {
        pwd = value
    }

    fun signIn() {
        lateinit var ret: SignInResponse
        viewModelScope.launch {
            ret = signInRepository.signIn(id, pwd)
            Log.d("signIn", "$ret")
            if (ret.message == "SUCCESS") {
                _signInSuccess.value = true
                setUserKey(ret.data)
            }
        }
    }

    suspend fun setUserKey(key: Int) {
        _keyInput.value = _keyInput.value.copy(userKey = key)
        keyRepository.insertKey(Key(1, key))
    }
}
