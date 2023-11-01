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
import kotlinx.coroutines.launch

class LoginViewModel(private val signInRepository: SignInRepository): ViewModel() {
    var SigninSuccess: Boolean by mutableStateOf(false)

    fun signIn(email: String, pw: String) {
        viewModelScope.launch {
            val ret: SignInResponse = signInRepository.signIn(email, pw)

            Log.d("signIn", "$ret")
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