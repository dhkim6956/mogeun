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

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            LoginViewModel(
                mogeunApplication().container.userDataRepository
            )
        }
        initializer {
            RecordViewModel(
                mogeunApplication().container.recordRepository
            )
        }
        initializer {
            ConnectionViewModel(
                mogeunApplication().container.emgDataRepository
            )
        }
        initializer {
            DbSampleViewModel(
                mogeunApplication().container.emgDataRepository
            )
        }
    }
}

fun CreationExtras.mogeunApplication(): MogeunApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as MogeunApplication)
