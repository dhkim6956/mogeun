package io.ssafy.mogeun.ui.screens.setting.setting

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.data.KeyRepository
import io.ssafy.mogeun.data.RoutineRepository
import io.ssafy.mogeun.data.UserRepository
import io.ssafy.mogeun.ui.screens.routine.searchRoutine.RoutineViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel(
    private val keyRepository: KeyRepository
) : ViewModel()  {
    fun deleteUserKey() {
        viewModelScope.launch {
            keyRepository.deleteKeyData()
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
                val keyRepository = application.container.keyRepository
                SettingViewModel(keyRepository)
            }
        }
    }
}