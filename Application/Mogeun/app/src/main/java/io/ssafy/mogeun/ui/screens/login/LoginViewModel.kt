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

class LoginViewModel(private val signInRepository: SignInRepository): ViewModel() {
    private val _signInSuccess = MutableStateFlow(false)
    val signInSuccess: StateFlow<Boolean> = _signInSuccess.asStateFlow()

    fun signIn(email: String, pw: String) {
        lateinit var ret: SignInResponse
        viewModelScope.launch {
            ret = signInRepository.signIn(email, pw)

            if(ret.message == "SUCCESS") {
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