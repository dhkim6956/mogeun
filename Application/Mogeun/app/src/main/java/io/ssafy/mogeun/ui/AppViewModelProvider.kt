package io.ssafy.mogeun.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.ssafy.mogeun.MogeunApplication
import io.ssafy.mogeun.ui.screens.login.LoginViewModel
import io.ssafy.mogeun.ui.screens.record.RecordViewModel
import io.ssafy.mogeun.ui.screens.sample.DbSampleViewModel
import io.ssafy.mogeun.ui.screens.setting.connection.ConnectionViewModel
import io.ssafy.mogeun.ui.screens.routine.addroutine.addexercise.AddExerciseViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            LoginViewModel(
                mogeunApplication().container.userDataRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            RecordViewModel(
                mogeunApplication().container.recordRepository,
                mogeunApplication().container.keyRepository
            )
        }
        initializer {
            ConnectionViewModel(
                mogeunApplication().container.emgDataRepository,
                mogeunApplication().container.bluetoothController
            )
        }
        initializer {
            DbSampleViewModel(
                mogeunApplication().container.emgDataRepository
            )
        }
        initializer {
            AddExerciseViewModel(
                mogeunApplication().container.listAllExerciseRepository
            )
        }
    }
}

fun CreationExtras.mogeunApplication(): MogeunApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)